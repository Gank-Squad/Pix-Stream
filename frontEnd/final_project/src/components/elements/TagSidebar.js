
import React from "react";

export default function VideoPlayer(props) 
{
    const [search, setSearch] = React.useState([]);
    const [tags, setTags] = React.useState({});
    const [tagDisplay, setTagDisplay] = React.useState(null);

    const TAGS_API = "http://192.168.1.148:8080/FinalProject-1.0-SNAPSHOT/api/tags";

    const tagSearch = React.useRef("");
    const selectedTagBox = React.useRef("");

    const tagClearButton = React.useRef();
    const tagSearchButton = React.useRef();

    const contextTagBox = React.useRef("");

    function clearSelected() 
    {
        const contextTags = selectedTagBox.current.querySelectorAll('tr');

        const tbody = contextTagBox.current.querySelector('tbody');

        contextTags.forEach(element => 
            {
            tbody.appendChild(element);
        });
    }

    function filterTags(entry) {
        let contextTags = contextTagBox.current.querySelectorAll('tr');

        contextTags.forEach(element => {
            const label = element.getElementsByTagName('label').item(0);

            if (!label)
                return;

            if (label.textContent.includes(entry)) {
                element.style.display = "inherit";
            }
            else {
                element.style.display = "none";
            }
        });
    }

    function updateSearch(tag_id, tag)
    {
        console.log("updating search with tag id: " + tag_id + " (not really this function does nothing you need to finish it)");

        setSearch(prevItems => [...prevItems, tag_id]);

        console.log(search);
    }

    function searchValueKeyDown(e) {
        filterTags(tagSearch.current.value);
    }

    function clearButtonPressed(e) {
        clearSelected();
    }

    function searchButtonPressed(e) {
        filterTags(tagSearch.current.value);
    }

    function tagButtonPressed(e) {
        if (!e || !e.target)
            return;

        let tag_id = e.target.getAttribute('data-key');

        const trSelected = selectedTagBox.current.querySelector(`tr[data-key="${tag_id}"]`);
        const trContext = contextTagBox.current.querySelector(`tr[data-key="${tag_id}"]`);

        if (trSelected) {
            contextTagBox.current.querySelector('tbody').appendChild(trSelected);
        }
        else {
            selectedTagBox.current.querySelector('tbody').appendChild(trContext);

            // idk why this doesn't work all the time
            updateSearch(tag_id, tags[tag_id]);
        }
    }


    React.useEffect(() => {
        fetch(TAGS_API, { 'method': 'get' })
            .then(response => response.json())
            .then(json => {

                let mappedTags = {};

                console.log(json);

                let tagDisp = [];
                for (let tag of json) 
                {
                    mappedTags[tag.tag_id] = tag; 

                    tagDisp.push(
                        <tr key={tag.tag_id} data-key={tag.tag_id}>
                            <td>
                                <div className="tag">
                                    <i className="fa fa-tag"></i>
                                    <button data-key={tag.tag_id} onClick={tagButtonPressed} className="fa fa-plus">+</button>
                                    <label >
                                        {tag.namespace + ":" + tag.subtag}
                                    </label>
                                </div>
                            </td>
                        </tr>
                    );
                }

                setTags(mappedTags);

                setTagDisplay(tagDisp);
            })
            .catch(e => console.log(e));

    }, []);



    return (

        <div id="navbar-background" >

            {search}

            <div id="navbar-content">

                <div style={{ "flexGrow": 0, "flexShrink": 0, "height": "100px" }}>
                </div>

                <div className="tagbox-container">
                    <header className="tagbox-header">
                        <label>Selected Tags</label>

                        <button id="search-btn" ref={tagSearchButton} onClick={searchButtonPressed}>
                            <i className="fa fa-search" aria-hidden="true"></i>Search
                        </button>

                        <button onClick={clearButtonPressed} ref={tagClearButton}>Clear</button>
                    </header>

                    <table ref={selectedTagBox} className="tagbox">
                        <tbody>

                        </tbody>
                    </table>
                </div>

                <div id="search-box">
                    <input onChange={searchValueKeyDown} ref={tagSearch} id="tag-search" type="text" placeholder="Tag Search..."></input>
                </div>

                <div className="tagbox-container">
                    <header className="tagbox-header">
                        <label>Context Tags</label>
                    </header>

                    <table ref={contextTagBox} className="tagbox">
                        <tbody>
                            {tagDisplay}
                        </tbody>
                    </table>
                </div>


                <div className="navbar-spacer"></div>

                <div className="link"><a href="about.html">
                    <i className="fa fa-info-circle"></i>About</a>
                </div>


            </div>

        </div>
    );
}

