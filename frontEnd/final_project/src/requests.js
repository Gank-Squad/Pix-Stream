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


export function formatStringA(formatString, ...values) 
{
    for (let i = 0; i < values.length; i++) 
    {
        formatString = formatString.replace(`{${i}}`, values[i]);
    }
  
    return formatString;
}


export function formatStringB(formatString, ...values) 
{
    let index = 0;
    const formattedString = formatString.replace(/\{([^\}]*)\}/g, (match, placeholder) => 
    {
        const value = values[index++];

        if(!value)
        {
            return match;
        }

        return value;
    });
    
    return formattedString;
  }

export default postData;