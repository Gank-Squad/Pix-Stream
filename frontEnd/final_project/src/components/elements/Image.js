import React from 'react'

export default function ImageContainer(props)
{
    const { image, caption, style  } = props;

    function imgError(image) 
    {
        return true;
    }

    return (
        <div className="file-container" style={style} loading="lazy">
                <img src={image} width={"100%"} onError={imgError} alt={"failed to load"} loading="lazy" />
                <div className="caption">{caption}</div>
        </div>
    )
}