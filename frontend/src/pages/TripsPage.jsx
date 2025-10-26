import { Container, Typography, Box, Button } from "@mui/material";
import { MuiNavbar } from "../components/MuiNavbar";

function TripsPage() {
    
    return (
        <>
            <MuiNavbar />
            <Container maxWidth={false} sx= {{display:"flex", alignItems:"left", justifyContent:"top", height: "100vh", width:"100vw", flexDirection:"column"}}>
                <Box sx={{ display: "flex", justifyContent: "start", mt: 2, mb: 3 }}>
                    <Typography variant="h4" fontWeight="bold">
                        Trips
                    </Typography>
                    <Button variant="contained" color="primary" sx={{ ml: 1 }}>
                        Create Trip
                    </Button>
                </Box>
            </Container>
        </>
    );
}

export default TripsPage;