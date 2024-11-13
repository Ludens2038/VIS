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

int main(int _argc, char** _argv) {
    int port = 4949;
    string ip = "127.0.0.1";

    //creating a active socket for the client
    cout << "starting up primitive socket client ... " << endl;
    int activeSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    if (activeSocket == -1)
    {
        cout << "error while generating socket handle" << endl;
        cout << "ERROR: " <<strerror(errno) << endl;
        return -1;
    }


    //configuration from the target server
    sockaddr_in serverAddr;
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(port);
    socklen_t size = sizeof(serverAddr);
    int rVal = inet_pton(AF_INET, ip.c_str(), (void*)&serverAddr.sin_addr.s_addr);
    if (rVal == -1)
    {
        cout << "error while encoding ip address ..." << endl;
        cout << "ERROR: " <<strerror(errno) << endl;
        return -1;
    }

    //tries to connect
    rVal = connect(activeSocket, (sockaddr*)&serverAddr, size);
    if (rVal == -1)
    {
        cout << "error while connecting to server ..." << endl;
        cout << "ERROR: " <<strerror(errno) << endl;
        return -1;
    } else
    {
        cout << "client connected to server" << endl;

        char comBuffer[BUFFER_SIZE];
        //endless loop
        while (true)
        {
            cout << "enter message :";
            //read in
            cin.getline(comBuffer, BUFFER_SIZE);
            string msg(comBuffer);

            if (msg.compare("quit") == 0) { //msg == quit
                close(activeSocket); //client shuts down
                break;
            }

            rVal = send(activeSocket, msg.c_str(), msg.length(), 0);
            if (rVal == -1)
            {
                // shit happened while sending data ...
                cout << "server disconnected unexpected while send" << endl;
                cout << "ERROR: " <<strerror(errno) << endl;
                close(activeSocket);
                break;
            } else
            {
                // data successfully sent ...
                cout << "sent" << rVal << " bytes to server" << endl;

                rVal = recv(activeSocket, comBuffer, BUFFER_SIZE, 0);
                if (rVal == -1)
                {
                    // connection broke unexpected
                    cout << "server disconnected unexpected while recieving" << endl;
                    cout << "ERROR: " <<strerror(errno) << endl;
                    close(activeSocket);
                    break;
                } else if (rVal == 0)
                {
                    // connection was closes gracefully
                    cout << "client disconnected ..." << endl;
                   break;
                } else
                {
                    // got some data
                    string msg = string(comBuffer,0,rVal);
                    cout << "server sent" << rVal << " bytes, message: " << msg << endl;
                } // else recv
            }
        }
        }


    cout << "shutting down client ... bye! " << endl;
    return 0;
}