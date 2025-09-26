import { AppBar, Button, Toolbar, Typography, Stack } from "@mui/material"
import MyLogo from "../assets/Logo.png"
import { clearToken } from "../utils/authUtils"
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
                <Button edge='start' aria-label='logo'>
                    <img src={MyLogo} style={{ height: 50, width: "auto "}}/>
                </Button>
                <Typography variant="h6" component='div' sx={{ flexGrow: 1 }}>
                    Basepaths
                </Typography>
                <Stack direction='row' spacing={2}>
                    <Button color='inherit' onClick={handleLogout}>
                        Logout
                    </Button>
                </Stack>
            </Toolbar>
        </AppBar>
    )
}