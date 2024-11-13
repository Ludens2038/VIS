#include <iostream> // cout, cin
#include <sys/socket.h> // socket, bind, listen, accept
#include <netinet/in.h> // IPPROTO_TCP, sockaddr_in,
// htons/ntohs, INADDR_ANY
#include <unistd.h> // close
#include <arpa/inet.h> // inet_ntop/inet_atop
#include <string.h> // strlen
#include <semaphore.h> // sem_init
#include <pthread.h>

#define BUFFER_SIZE 1024 //buffer for messages

#define BACKLOG 13 //max amount of incoming connections

using namespace std;

int main(int _argc, char **_argv) {
    int port = 4949; // initializing the port

    //start message
    cout << "starting up primitive socket sever ... " << endl;

    /*creating passive socket.
     *AF_INET stands für IPv4
     *SOCK_STREAM stands for TCP
     *protocol "0" lets method decide what protocol it is using. here it will obvsly use TCP bc SOCK_STREAM */
    int passiveSocket = socket(AF_INET, SOCK_STREAM, 0);

    /*if connection fails, socket call returns -1 */
    if (passiveSocket == -1) {
        cout << "error while generating socket handle" << endl;
        cout << "ERROR = " << strerror(errno) << endl;
        return -1;
    }

    sockaddr_in addr; // create a struct (unique class) and name it addr

    addr.sin_family = AF_INET; //ipv4

    addr.sin_port = htons(port); //converting from host-byte to network-byte. saved as big-endian

    /* INADDR_ANY accept all local connections. now it is able to listen to all incoming demands
     default value is 0.0.0.0 so it accepts everything. in our scenario we work with the loopback: 127.0.0.1*/
    addr.sin_addr.s_addr = INADDR_ANY;

    /* to define size of the struct. so the OS knows how many bytes it has to read. comparable to size in a header*/
    socklen_t size = sizeof(addr);

    /* binds the address to the specific port. rVal is going to be reassigned multiple times to save up
     memory. its purpose is to store return values of bind, listen, accept and send*/
    int rVal = bind(passiveSocket, (sockaddr *) &addr, size);

    if (rVal == -1) //if it is failing
    {
        cout << "error while binding socket handle" << endl;
        cout << "ERROR = " << strerror(errno) << endl;
        return -1;
    }

    /*sets the passive Socket to listen mode. waiting for incoming connection attempts and
     stores them into a queue. max queue is equal to backlog*/
    rVal = listen(passiveSocket, BACKLOG);
    if (rVal == -1) {
        cout << "error while setting socket handle to listen mode " << endl;
        cout << "ERROR = " << strerror(errno) << endl;
        return -1;
    }

    /*declaring a clientAddr struct which stores the address of the client. it contains fields like
     ip-address, port-number. this information is automatically stored by the OS if the server accepts the connection*/
    sockaddr_in clientAddr;
    size = sizeof(clientAddr); //again to remember the size of the address
    cout << "server waiting for client on port ... " << port << endl;

    bool running = true;
    /*infinity-loop that sets the server to uninterrupted communication status*/
    while (running) {
        /*opens a new active socket for client server communication.
         1. parameter: passive socket = socketnumber of listening server
         2. parameter: pointer to clientAddr. accept expects a sockAddr therefore the typecast
         3. parameter: size of the address. again for the OS to know how long the address is
         */
        int activeSocket = accept(passiveSocket, (sockaddr *) &clientAddr, &size); //& = pointer
        if (activeSocket == -1) {
            cout << "error while accepting active socket handle  " << port << endl;
            cout << "ERROR = " << strerror(errno) << endl;
            return -1;
        } else {
            //positive connection
            cout << "client connected" << endl;
            char comBuffer[BUFFER_SIZE]; // initialize a char array with size BUFFER_SIZE
            while (true) { // another infinity-loop
                /*recv is a method that reads incoming msgs from connected socket
                 * 1. parameter: socket. simply the active socket where the msgs come from
                 * 2. parameter: char array that contains content from socket (incoming from client)
                 * 3. parameter: length of the char-array
                 * 4. parameter: flags. no specific flag has been chosen. 0 stands for standard reading of rcv
                 * recv returns -1 if fails */
                rVal = recv(activeSocket, comBuffer, BUFFER_SIZE, 0);

                if (rVal == -1) { //in case connection fails
                    cout << "client disconnected unexpected  " << port << endl;
                    cout << "ERROR = " << strerror(errno) << endl;
                    return -1;
                    break;
                } else if (rVal == 0) { //connection was closed gracefully
                    cout << "client disconnected  " << port << endl;
                    break;
                } else {//recieved data and stored it into msg. afterwards print msg
                    string msg = string(comBuffer, 0, rVal);
                    cout << "client sent massage: " << comBuffer << endl;

                    if (msg == "drop") { //if client send drop, the active socket is closed
                        close(activeSocket);
                        break;
                    } else if (msg == "shutdown") { // shutting down both sockets
                        close(activeSocket);
                        close(passiveSocket);
                    } else { // server replies to client with echo...
                        string reply = "Echo...";
                        reply.append(msg);
                        /*sends msg to client
                         1. parameter: the active socket
                         2. parameter: pointer to string incl null-termination (.c_str())
                                       so the program knows where reserved memory ends
                         3. parameter: length of the actual string
                         4. parameter: flag parameter... dunno what it does
                         */
                        rVal = send(activeSocket, msg.c_str(), msg.length(), 0);
                    }
                    if (rVal == -1) {
                        //shit happend while sendig data
                        cout << "client disconnected unexpected  " << port << endl;
                        cout << "ERROR = " << strerror(errno) << endl;
                        return -1;
                        break;
                    } else {
                        cout << "client sent massage: " << rVal << " bytes to Server" << endl;
                        // data succesfully sent
                    }
                }
            } //while true
        } //else recv
    } //while running
    cout << "shutting down server ... bye!" << endl;
    return 0;
}
