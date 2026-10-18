import socket
import sys

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server.bind(('0.0.0.0', 47800))
server.listen(5)
print("Listening on 0.0.0.0:47800...", flush=True)

while True:
    conn, addr = server.accept()
    print(f"Connection accepted from {addr}", flush=True)
    try:
        data = conn.recv(1024)
        if data:
            print(f"Received: {data.decode('utf-8', errors='ignore').strip()}", flush=True)
            conn.sendall(b"HELLO gamestream-host 1\n")
            print("Sent HELLO response", flush=True)
    except Exception as e:
        print(f"Error handling client {addr}: {e}", flush=True)
    finally:
        conn.close()
