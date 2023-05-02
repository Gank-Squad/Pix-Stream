import requests

def ww():

    p = "02f8877e7440cb53b5c59a800dbf5b98f7ab6516c408cc068aed910cf081afd1"
    
    rul = "http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/files/t/" + p 

    e = requests.get(rul)

ww()