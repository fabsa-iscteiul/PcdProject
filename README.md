# PcdProject
Sharing files between server and client

On the server side it expects the two port numbers (one for the file transfer socket and the other for the notification socket) and the name of the directory that contains the file (PCDFiles) as arguments to run.

On the client side it expects the address to connect to the server and the port numbers as arguments to run.

# To run the server

java -jar server_jar.jar 124 125 PCDFiles

# To run the client 
java -jar client_jar.jar localhost 124 125
