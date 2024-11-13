#include <iostream> // cout, cin
#include <sys/socket.h> // socket, bind, listen, accept
#include <netinet/in.h> // IPPROTO_TCP, sockaddr_in,
// htons/ntohs, INADDR_ANY
#include <unistd.h> // close
#include <arpa/inet.h> // inet_ntop/inet_atop
#include <string.h> // strlen
#include <semaphore.h> // sem_init
#include <pthread.h>
#include <cstdlib>
#include <ctime>

#define BUFFER_SIZE 1024 //puffer for messages

#define BACKLOG 13 //max amount of incoming connections

using namespace std;

int main(int _argc, char **_argv) {
    int port = 4949;

    //initialize random number generator
    std::srand((std::time(0)));

    //startup message
    cout << "starting up primitive socket sever ... " << endl;

    //creating a passive socket waiting for connections
    int passiveSocket = socket(AF_INET, SOCK_STREAM, 0);
    //if the connection fails
    if (passiveSocket == -1) {
        cout << "error while generating socket handle" << endl;
        cout << "ERROR = " << strerror(errno) << endl;
        return -1;
    }

    //configure the addr from the server
    sockaddr_in addr;
    addr.sin_family = AF_INET; //ipv4
    addr.sin_port = htons(port); //set the port, htons -> convert in byte sequence
    addr.sin_addr.s_addr = INADDR_ANY; //accept connections from any ip-address
    socklen_t size = sizeof(addr);

    //to bind the socket to the specific addr
    int rVal = bind(passiveSocket, (sockaddr *) &addr, size);
    if (rVal == -1) //if it is failing
    {
        cout << "error while binding socket handle" << endl;
        cout << "ERROR = " << strerror(errno) << endl;
        return -1;
    }

    //put the socket into listen mode
    rVal = listen(passiveSocket, BACKLOG);
    if (rVal == -1) {
        cout << "error while setting socket handle to listen mode " << endl;
        cout << "ERROR = " << strerror(errno) << endl;
        return -1;
    }


    sockaddr_in clientAddr; // struct that stores client address
    size = sizeof(clientAddr); // getting size for further cpp operations
    cout << "server waiting for client on port ... " << port << endl;

    bool running = true;
    while (running) {
        //accept an incoming connection and create an active socket for the communication
        int activeSocket = accept(passiveSocket, (sockaddr *) &clientAddr, &size); //& = pointer
        if (activeSocket == -1) {
            cout << "error while accepting active socket handle  " << port << endl;
            cout << "ERROR = " << strerror(errno) << endl;
            return -1;
        } else {
            //positive connection
            cout << "client connected" << endl;
            char comBuffer[BUFFER_SIZE]; // communication buffer to store incoming msg

            while (true) {
                rVal = recv(activeSocket, comBuffer, BUFFER_SIZE, 0);

                if (rVal == -1) {
                    //connection broke unexpected
                    cout << "client disconnected unexpected  " << port << endl;
                    cout << "ERROR = " << strerror(errno) << endl;
                    return -1;
                    break;
                } else if (rVal == 0) {
                    //connection was closed gracefully
                    cout << "client disconnected  " << port << endl;
                    break;
                } else {
                    //got some data
                    string msg = string(comBuffer, 0, rVal);
                    cout << "client sent massage: " << comBuffer << endl;

                    if (msg == "drop") {
                        close(activeSocket);
                        break;
                    } else if (msg == "shutdown") {
                        close(activeSocket);
                        close(passiveSocket);
                    } else {
                        string reply; // to store answer of server
                        int ix = msg.find("MyMethod");

                        if (ix >= 0) {
                            //found protocol
                            cout << "found command" << "MyMethod" << endl;
                            int ixA = msg.find("(");
                            int ixB = msg.find(")");
                            string param = string(msg.c_str(), ixA, ixB);
                            cout << "found parameter: " << param << " ... processing ..." << endl;

                            //generatte random number 0 ... 100
                            int rnd = rand() % 101;
                            reply = to_string(rnd) + " answer to " + msg;

                        } else {
                            //go
                            reply = "Echo ...";
                            reply.append(msg);
                        }

                        rVal = send(activeSocket, reply.c_str(), reply.length(), 0);
                        if (rVal == -1) {
                            //shit happend while sendig data
                            cout << "client disconnected unexpected  " << port << endl;
                            cout << "ERROR = " << strerror(errno) << endl;
                            return -1;
                            break;
                        } else {
                            cout << "server sent massage: " << rVal << " bytes to client" << endl;
                            // data succesfully sent
                        }
                    } // else command interpretation
                } // else rcv
            } // while true
        } // else accept
    } // while running

    cout << "shutting down server ... bye!" << endl;
    return 0;
}
