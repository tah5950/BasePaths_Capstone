import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Box,
  Typography,
  TextField,
  Button,
  Alert,
} from "@mui/material";
import { saveToken } from "../utils/authUtils";
import { API_BASE_URL } from '../config';

function CreateAccount() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(`${API_BASE_URL}/api/user/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      });

      if (response.ok) {
        const data = await response.json();
        const token = data.token;
        saveToken(token);
        navigate("/home");
      } else {
        const error = await response.text();
        setMessage(`Error: ${error}`);
      }
    } catch (err) {
      setMessage("Error: " + err.message);
    }
  };

  return (
    <Box display="flex" justifyContent="center" alignItems="center" height= "100vh" width="100vw" flexDirection="column">
        <Box  display="flex" justifyContent="center" flexDirection="column" alignItems="center" height= "300px" width="400px" p={3} boxShadow={3} borderRadius={2} >
            <Typography component="h1" variant="h4" gutterBottom>
                Create Account
            </Typography>
            <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
                <TextField
                    margin="normal"
                    fullWidth
                    label="Username"
                    name="username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required/>
                <TextField
                    margin="normal"
                    fullWidth
                    label="Password"
                    name="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required/>
                <Button
                    type="submit"
                    fullWidth
                    variant="contained"
                    label="Create"
                    sx={{ mt: 3, mb: 2 }}>
                    Create
                </Button>
            </Box>
            {message && (
                <Alert severity={message.startsWith("Error") ? "error" : "success"}>
                    {message}
                </Alert>
            )}
        </Box>
    </Box>
  );
}

export default CreateAccount;
