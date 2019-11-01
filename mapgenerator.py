import folium
from geopy.geocoders import Nominatim
from folium.features import PolyLine
import socket

def show_map(data):
    data = data.split(", ")
    Kazakhstan = folium.Map(location=[47.49, 66.36],
                            zoom_start=5,
                            tiles='OpenStreetMap')
    Kazakhstan.zoom_start = 15
    path = []
    ini = 0
    fi = len(data)
    for i, s in enumerate(data[:len(data)-2]):
        print(s)
        if data[len(data)-2] == s:
            ini = i
        elif data[len(data)-1] == s:
            fi = i
            break
    d = data[int(ini):int(fi)+1]
    print(d)
    for i, s in enumerate(d):
        loc = Nominatim(user_agent="my-application").geocode(s, timeout=1000)
        if i == 0 or i == len(data) - 1:
            spiced = folium.Marker((loc.latitude, loc.longitude), popup='Departure point',
                                   icon=folium.Icon(icon='fire',
                                                    color='orange'))
        else:
            spiced = folium.Marker((loc.latitude, loc.longitude),
                                   icon=folium.Icon(icon='fire',
                                                    color='blue'))
        spiced.add_to(Kazakhstan)

        path.append((loc.latitude, loc.longitude))

    line = PolyLine(path,
                    popup="my favourite ice cream shop",
                    color="blue",
                    weight=5,
                    opacity=0.8)

    line.add_to(Kazakhstan)
    Kazakhstan.save('map.html')
    return Kazakhstan


soc = socket.socket()
host = "localhost"
port = 2004
soc.bind((host, port))
soc.listen(5)

while True:
    conn, addr = soc.accept()
    print("Got connection from",addr)
    length_of_message = int.from_bytes(conn.recv(2), byteorder='big')
    msg = conn.recv(length_of_message).decode("UTF-8")
    print(msg)
    print(length_of_message)
    show_map(msg)
    f = open("map.html", 'rb').read()
    conn.send(len(f).to_bytes(2, byteorder='big'))
    conn.send(f)
    print("DDDDDDDDDDD")
