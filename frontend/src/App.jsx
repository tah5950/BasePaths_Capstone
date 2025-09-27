import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import CreateAccount from "./pages/CreateAccount";
import Login from "./pages/Login";
import Home from "./pages/Home";
import RequireAuth  from "./utils/RequireAuth";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<RequireAuth><Home/></RequireAuth>} />
        <Route path="/login" element={<Login />} />
        <Route path="/createaccount" element={<CreateAccount />} />
        <Route path="/home" element={<RequireAuth><Home/></RequireAuth>} />
      </Routes>
    </Router>
  );
}

export default App;
