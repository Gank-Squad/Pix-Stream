import requests

DATA = "./data_files/"

def upload():

    file = DATA + "/img/2.jpg"
    file = DATA + "/bad/conjure.exe"
    file = DATA + "/vid/1.mp4"
    file = DATA + "/vid/2.mkv"
    # file = DATA + "/img/4.jpg"

    payload = {
        "data": open(file, "rb")
    }

    a = requests.post("http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/media/upload3/", files=payload)

    print(a.status_code)
    print(a.text)


upload()