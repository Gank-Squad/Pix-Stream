import React from 'react';

import TagSidebar from '../elements/TagSidebar';
import ImageContainer from '../elements/Image';

export default function Default(props)
{
    const { cookies } = props;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;


    React.useEffect(() => 
    {
        console.log(`Page ${page} was loaded!`);
        console.log(`Cookies from props: ${cookies}`);
    }, [cookies, page]);

    function searchCallback(searchItems)
    {
        searchItems.forEach(element => 
        {
            console.log("from parent: " + JSON.stringify(element));
        });
    }


    const tagSidebarProps = {
        "searchCallback" : searchCallback
    }

    return (
        <div>

            Hello world, you are on home page {(page).toString()}

            <TagSidebar {...tagSidebarProps}></TagSidebar>

            <ImageContainer imgError={e => console.log("image errr")}></ImageContainer>

        </div>
    )
}