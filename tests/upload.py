import requests

DATA = "./data_files/img/"

def upload():

    file = DATA + "2.jpg"

    payload = {
        "file": open(file, "rb")
    }

    a = requests.post("http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/media/upload2/", files=payload)

    print(a.status_code)
    print(a.text)


upload()