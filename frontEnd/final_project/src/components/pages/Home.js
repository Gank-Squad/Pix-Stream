import React from 'react';

import TagSidebar from '../elements/TagSidebar';

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

    return (
        <div>

            Hello world, you are on home page {(page).toString()}

            <TagSidebar></TagSidebar>

        </div>
    )
}