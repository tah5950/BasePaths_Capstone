import { Container, Typography } from "@mui/material";
import { MuiNavbar } from "../components/MuiNavbar";

function Home() {
  
  return (
    <>
    <MuiNavbar />
    <Container maxWidth={false} sx= {{display:"flex", alignItems:"center", justifyContent:"center", height: "100vh", width:"100vw", flexDirection:"column"}}>
      <Typography variant="h3" component="h1">
        Welcome to Basepaths
      </Typography>
    </Container>
    </>
  );
}

export default Home;
