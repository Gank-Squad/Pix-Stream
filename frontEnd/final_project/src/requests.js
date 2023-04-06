function postData(url, data, headers = {}, progress = null) 
{
    // blessed promise so you can await this qt
    return new Promise(function (resolve, reject) 
    {
        let xhr = new XMLHttpRequest();
        
        xhr.open('POST', url, true);

        // set request headers given
        Object.keys(headers).forEach((key) => 
        {
            xhr.setRequestHeader(key, headers[key]);
        });

        // set update handler 
        xhr.upload.onprogress = progress;

        // handle resolve and reject
        xhr.onload = function () 
        {
            const status = xhr.status;
            
            if (status === 200) 
            {
                resolve(xhr);
            } 
            else 
            {
                reject(xhr);
            }
        };

        xhr.send(data);
    });
}

export default postData;