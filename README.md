
Developpement of a simplified HDLC protocol.
 Used sockets to establish connexion between a client and a server.
 The data is parsed and bits control are added before being sent as in frame
 Client roles:
    -read data from file
    -generate frame before sending them
    -wait and treat the server response
    -resend lost data
 Server roles:
    -receive frames
    -verify if the received data is correct
    -send acknoledgments
    -Send SREJ (bits that indicate the type of error when the data is not correct)



