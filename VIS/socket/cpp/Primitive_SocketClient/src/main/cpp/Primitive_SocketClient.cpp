#include <iostream> // cout, cin
#include <sys/socket.h> // socket, bind, listen, accept
#include <netinet/in.h> // IPPROTO_TCP, sockaddr_in,
// htons/ntohs, INADDR_ANY
#include <unistd.h> // close
#include <arpa/inet.h> // inet_ntop/inet_atop
#include <string.h> // strlen
#include <semaphore.h> // sem_init
#include <pthread.h>

#define BUFFER_SIZE 1024

using namespace std;

int main(int _argc, char **_argv) {
    int port = 4949; // initializing the port
    string ip = "127.0.0.1"; // initializing the ip address of the client. here loopback bc we simulate comm locally
    cout << "starting up primitive socket client ... " << endl;

    /*creating an active socket
     * 1. parameter: domain, AF_INET stands for IPv4
     * 2. parameter: type, in our case TCP
     * 3. parameter: protocol, we set the protocol to tcp optionally; if 0 method implies that its tcp bc of type
     * its return value is stored in activeSocket */
    int activeSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    if (activeSocket == -1) {
        cout << "error while generating socket handle" << endl;
        cout << "ERROR: " << strerror(errno) << endl;
        return -1;
    }

    /*sockaddr_in is a struct that contains:
     * 1. sin_family: IPv4 or IPv6
     * 2. sin_port: htons() converting from host-byte to network-byte. stores as big-endian */
    sockaddr_in serverAddr;
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(port);
    socklen_t size = sizeof(serverAddr); // defining size of the struct. cpp needs this value for futher operations

    /*converting server ip to a format the socket understands
     * 1. parameter: addressfamily, in our case again ipv4
     * 2. parameter: source, the ip in textual form, this is going to be translated into binary code
     * 3. paramter: destination, a pointer to RAM area where the address is stored in binary code(in network-byte format) */
    int rVal = inet_pton(AF_INET, ip.c_str(), (void *) &serverAddr.sin_addr.s_addr);
    if (rVal == -1) {
        cout << "error while encoding ip address ..." << endl;
        cout << "ERROR: " << strerror(errno) << endl;
        return -1;
    }

    /*establishing connection to server
     * 1. parameter: socket-descriptor
     * 2. parameter: pointer to destination, containing ip and socket, simply the server we want to connect to
     * 3. parameter: size of sockaddr struct, cpp needs this to know how many bytes it has to read */
    rVal = connect(activeSocket, (sockaddr *) &serverAddr, size);
    if (rVal == -1) {
        cout << "error while connecting to server ..." << endl;
        cout << "ERROR: " << strerror(errno) << endl;
        return -1;
    } else {
        cout << "client connected to server" << endl;

        char comBuffer[BUFFER_SIZE]; // buffer for client input

        while (true) {
            // ininity loop for communication
            cout << "enter message :";

            /*following is comparable to in.read.something from Java
             * cpp needs the var where to store the input und die buffer size damit es nicht zu viel einliest und
             * der puffer nicht überlaufen wird */
            cin.getline(comBuffer, BUFFER_SIZE);

            /*converting the msg array into a cpp object*/
            string msg(comBuffer);

            // if the message contains quit, the client shuts down
            if (msg.compare("quit") == 0) {
                //msg == quit
                close(activeSocket); //client shuts down
                break;// leaving the while loop
            }

            /*sending msg to server
             * 1. parameter: the one and only socketman
             * 2. parameter: pointer to stored string incl null-termination
             *               .c_str() tells where the reserved storage ends
             * 3. parameter: actual length of the string
             * 4. parameter: the one and only flag, noone knows what this does...
             *               biggest mistery of mankind
             */
            rVal = send(activeSocket, msg.c_str(), msg.length(), 0);
            if (rVal == -1) {
                // shit happened while sending data ...
                cout << "server disconnected unexpected while send" << endl;
                cout << "ERROR: " << strerror(errno) << endl;
                close(activeSocket);
                break;
            } else {
                // data successfully sent ...
                cout << "sent" << rVal << " bytes to server" << endl;

                /*recv is a method that reads incoming msgs from connected socket
                 * 1. parameter: socket. simply the active socket where the msgs come from
                 * 2. parameter: char array that contains content from socket (incoming from client)
                 * 3. parameter: length of the char-array
                 * 4. parameter: flags. no specific flag has been chosen. 0 stands for standard reading of rcv
                 * recv returns -1 if fails */
                rVal = recv(activeSocket, comBuffer, BUFFER_SIZE, 0);
                if (rVal == -1) {
                    // connection broke unexpected
                    cout << "server disconnected unexpected while recieving" << endl;
                    cout << "ERROR: " << strerror(errno) << endl;
                    close(activeSocket);
                    break;
                } else if (rVal == 0) {
                    // connection was closes gracefully
                    cout << "client disconnected ..." << endl;
                    break;
                } else {
                    /*A new std::string object named msg is created.
                     *A special std::string constructor is used here,
                     *which creates a std::string from a C string (character array) and an explicit length specification.*/
                    string msg = string(comBuffer, 0, rVal);
                    cout << "server sent" << rVal << " bytes, message: " << msg << endl;
                } // else recv
            }
        }
    }


    cout << "shutting down client ... bye! " << endl;
    return 0;
}
