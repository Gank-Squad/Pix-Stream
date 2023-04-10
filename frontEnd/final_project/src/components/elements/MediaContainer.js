import React from 'react'

import VideoPlayer from './Video';
import { DISPLAY_TYPES,
    API_TEMPLATES,
    MIME_IMG_CUTOFF,
    NO_MEDIA_IMG } from '../../constants';
import { formatStringB } from '../../requests';

export default function VideoContainer(props)
{
    // const { hlsUrl, hlsDomain, caption, style  } = props;
    // const { image, caption, style  } = props;
    const { 
        hash, 
        hlsDomain, 
        displayType, 
        metaData:{
            id,
            title,
            description,
            created_at,
            mime_int,
            duration,
            width,
            height
        }
    } = props;

    const max_width = 8;
    const max_height = 256;

    function imgError(image) 
    {
        // set the source to no media image, disregard error
        return true;
    }

    function thumbnailPreview()
    {
        // should return div that is of a set width and height
        // div should contain image, which has at least one of its dimensions equal to widht or height of container
        
        //get thumbnail url
        const thumb_url = formatStringB(API_TEMPLATES.get_thumbnail.url, hash);

        const dx = Math.abs(max_width - props.metaData.width);
        const dy = Math.abs(max_height - props.metaData.height);

        if (dx > dy)
        {
            // display width should = max_width
            return (
                // make it a fixed width and height for consistent tiling
                <div className="border-dashed border-2 border-white" width={max_width} height={max_height}>
                    <img
                        className="border-dashed border-2 border-white"
                        src={thumb_url} width={max_width} onError={imgError} loading="lazy" />
                    {/* <div className="text-custom-white caption">caption</div> */}

                </div>
            );
        }
        else
        {
            // display height should = max_height
            return (
                // make it a fixed width and height for consistent tiling
                <div className="border-solid border-2 border-white" width={max_width} height={max_height}>
                    <img
                        className="border-dashed border-2 border-white"
                        src={thumb_url} height={max_height} onError={imgError} loading="lazy" />
                    {/* <div className="text-custom-white caption">caption</div> */}

                </div>
            );
        }

    }
    function generalDisplay()
    {
        return (<div></div>)
    }

    function fullDisplay()
    {
        return (<div></div>)
    }

    function getCorrectDisplay(displayType)
    {
        switch (displayType)
        {
            case DISPLAY_TYPES.thumb_preview:
                return thumbnailPreview();
            case DISPLAY_TYPES.general_display:
                return generalDisplay();
            case DISPLAY_TYPES.full_size_display:
                return fullDisplay();
            default:
                return thumbnailPreview();
        }
    }

    return getCorrectDisplay();

    // function imgError(image) 
    // {
    //     return true;
    // }

    // return (
    //     <div className="file-container" style={style} loading="lazy">
    //             <img src={image} width={"100%"} onError={imgError} alt={"failed to load"} loading="lazy" />
    //             <div className="text-custom-white caption">{caption}</div>
    //     </div>
    // )
}