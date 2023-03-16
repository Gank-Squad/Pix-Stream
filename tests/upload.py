import requests

DATA = "./data_files/"

def upload():

    file = DATA + "/img/2.jpg"

    payload = {
        "data": open(file, "rb")
    }

    a = requests.post("http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/media/upload3/", files=payload)

    print(a.status_code)
    print(a.text)


upload()