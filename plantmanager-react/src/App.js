import './App.css';
import Login from './components/Login/Login';
import ForgotPassword from './components/ForgotPassword/ForgotPassword';
import Homepage from './components/Homepage/Homepage';
import AppHeader from './components/AppHeader/AppHeader';
import {DependencyProvider} from "./DependencyContext";
import AuthService from "./services/AuthService/AuthService";

import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Dashboard from "./components/Dashboard/Dashboard";


function App() {
    return (
        <Router>
            <DependencyProvider authService={AuthService}>
                <div className="App">
                    <AppHeader/>
                    <Routes>
                        <Route path="/" element={<Homepage/>}/>
                        <Route path="/login/:where" element={<Login/>}/>
                        <Route path="/forgotPassword" element={<ForgotPassword/>}/>
                        <Route path="/dashboard" element={<Dashboard/>}/>
                    </Routes>
                </div>
            </DependencyProvider>
        </Router>
    );
}

export default App;
