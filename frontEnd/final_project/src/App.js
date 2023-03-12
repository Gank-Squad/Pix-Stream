
import React from 'react';
import {BrowserRouter as Router,Routes,Route} from "react-router-dom";

import './App.css';

// import './css/main.css';
import './css/output.css';
// import './css/tailwind.css';

import Default from './components/pages/Default';
import Home from './components/pages/Home';
import Media from './components/pages/Media';
import PageNotFound from './components/pages/PageNotFound';

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
          <Route path="*" element={<PageNotFound />} />
          </Routes>
        </Router>
      );
}

export default App;
