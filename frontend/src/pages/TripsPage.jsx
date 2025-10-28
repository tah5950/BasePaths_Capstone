import { useState } from "react";
import { Container, Typography, Box, Button } from "@mui/material";
import { MuiNavbar } from "../components/MuiNavbar";
import CreateTripForm from "../components/CreateTripForm";

function TripsPage() {
    const [openForm, setOpenForm] = useState(false);

    const handleOpen = () => setOpenForm(true);
    const handleClose = () => setOpenForm(false);

    const handleTripCreated = (newTrip) => {
        console.log("Trip created: ", newTrip);
        // TODO: Refresh Trip List
    }
    
    return (
        <>
            <MuiNavbar />
            <Container maxWidth={false} sx= {{display:"flex", alignItems:"left", justifyContent:"top", height: "100vh", width:"100vw", flexDirection:"column"}}>
                <Box sx={{ display: "flex", justifyContent: "start", mt: 2, mb: 3 }}>
                    <Typography variant="h4" fontWeight="bold">
                        Trips
                    </Typography>
                    <Button onClick={handleOpen} variant="contained" color="primary" sx={{ ml: 1 }}>
                        Create Trip
                    </Button>
                </Box>
                <CreateTripForm
                    open={openForm}
                    onClose={handleClose}
                    onTripCreated={handleTripCreated}
                />
            </Container>
        </>
    );
}

export default TripsPage;