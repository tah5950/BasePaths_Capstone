import { AppBar, Button, Toolbar, Typography, Stack, Box } from "@mui/material";
import MyLogo from "../assets/Logo.png";
import { clearToken } from "../utils/authUtils";
import { useNavigate } from "react-router-dom";

export const MuiNavbar = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        clearToken();
        navigate("/login");
    };
    
    return(
        <AppBar position='static'>
            <Toolbar>
                <Box sx={{ display: "flex", alignItems: "center", flexGrow: 1 }}>
                    <Button edge='start' aria-label='logo' onClick={() => navigate("/home")}>
                        <img src={MyLogo} style={{ height: 50, width: "auto "}}/>
                    </Button>
                    <Typography variant="h6" component='div' sx={{ mr: 2, cursor: "default" }}>
                        Basepaths
                    </Typography>
                    <Button color='inherit' onClick={() => navigate("/trips")}>
                        Trips
                    </Button>
                </Box>
                <Stack direction='row' spacing={2}>
                    <Button color='inherit' onClick={handleLogout}>
                        Logout
                    </Button>
                </Stack>
            </Toolbar>
        </AppBar>
    )
}