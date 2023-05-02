
import requests
import os 


def upload(path: str):
    
    if not os.path.isfile(path):
        print(f"File {path} does not exist")
        raise Exception("File does not exist")

    payload = {
        "data" : open(path, "rb")
    }
    a = requests.post("http://192.168.1.148:8080/FinalProject-1.0-SNAPSHOT/api/files/upload/", files=payload)

    if a.status_code != 200:
        print(a.status_code)
        print(a)
    else:
        print(200)


dir = "./data_files/img/"
for path in os.listdir(dir):

    if path.endswith("png") or path.endswith("jpg"):

        upload(os.path.join(dir, path))

print(requests.get("http://192.168.1.148:8080/FinalProject-1.0-SNAPSHOT/api/tags/ass/?limit=20"))
