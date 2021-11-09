import './App.css';
import Login from './components/Login/Login';
import ForgotPassword from './components/ForgotPassword/ForgotPassword';
import Homepage from './components/Homepage/Homepage';
import AppHeader from './components/AppHeader/AppHeader';

function App() {
  return (
    <div className="App">
      <AppHeader/>
      <Login/>
      <ForgotPassword/>
      <Homepage/>
    </div>
  );
}

export default App;
