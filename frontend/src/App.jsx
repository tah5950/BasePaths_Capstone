import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import CreateAccount from "./pages/CreateAccount";
import Home from "./pages/Home";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/createaccount" element={<CreateAccount />} />
        <Route path="/home" element={<Home />} />
      </Routes>
    </Router>
  );
}

export default App;
