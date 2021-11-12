import './App.css';
import Login from './components/Login/Login';
import ForgotPassword from './components/ForgotPassword/ForgotPassword';
import Homepage from './components/Homepage/Homepage';
import AppHeader from './components/AppHeader/AppHeader';

import {BrowserRouter as Router, Route, Routes} from "react-router-dom";


function App() {
    return (
        <Router>
            <div className="App">
                <AppHeader/>
                <Routes>
                    <Route path="/" element={<Homepage />} />
                    <Route path="/login/:where" element={<Login />}/>
                    <Route path="/forgotPassword" element={<ForgotPassword />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
