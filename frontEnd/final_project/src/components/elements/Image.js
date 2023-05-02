import React from 'react'

// Deprecated code, do not use
// use MediaContainer instead
export default function ImageContainer(props)
{
    const { image, caption, style  } = props;

    function imgError(image) 
    {
        return true;
    }

    // return the picture
    return (
        <div className="file-container" style={style} loading="lazy">
                <img src={image} width={"100%"} onError={imgError} alt={"failed to load"} loading="lazy" />
                <div className="text-custom-white caption">{caption}</div>
        </div>
    )
}