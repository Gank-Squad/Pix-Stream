
import React from 'react';
import {BrowserRouter as Router,Routes,Route} from "react-router-dom";

// import './App.css';

// import './css/main.css';
import './css/output.css';
// import './css/tailwind.css';

import Default from './components/pages/Default';
import Home from './components/pages/Home';
import Media from './components/pages/Media';
import PageNotFound from './components/pages/PageNotFound';
import SearchResults from './components/pages/SearchResults';
import Tags from './components/pages/Tags';
import Upload from './components/pages/Upload';
import User from './components/pages/User';

import { ROUTES } from './constants';

function App() {
    const [state, setState] = React.useState({ 
        cookies : undefined // we can get cookies from a library and put them here
      });
      
      return (
        <Router>
          <Routes>
          <Route path={ROUTES.default} element={<Default {...state} />}></Route>
          <Route path={ROUTES.home} element={<Home {...state} />}></Route>
          <Route path={ROUTES.media} element={<Media {...state} />}></Route>
          <Route path={ROUTES.search_results} element={<SearchResults {...state} />}></Route>
          <Route path={ROUTES.tags} element={<Tags {...state} />}></Route>
          <Route path={ROUTES.upload} element={<Upload {...state} />}></Route>
          <Route path={ROUTES.user} element={<User {...state} />}></Route>
          <Route path="*" element={<PageNotFound />} />
          </Routes>
        </Router>
      );
}

export default App;
