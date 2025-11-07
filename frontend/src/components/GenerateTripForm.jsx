import { useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Box,
  Alert,
} from "@mui/material";
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { getToken } from "../utils/authUtils";
import { API_BASE_URL } from "../config";

const GenerateTripForm = ({ open, onClose, onTripGenerated, trip })  => {
  const [message, setMessage] = useState("");
  const [formData, setFormData] = useState({
    startLatitude: "",
    startLongitude: "",
    endLatitude: "",
    endLongitude: "",
    maxHoursPerDay: "",
  });

  const handleChange = (key, value) => {
      setFormData((prev) => ({...prev, [key]: value}))
  };

  const handleSubmit = async (e) => {
      e.preventDefault();
      
      const dto = {
          ...trip,
          startLatitude: parseFloat(formData.startLatitude),
          startLongitude: parseFloat(formData.startLongitude),
          endLatitude: parseFloat(formData.endLatitude),
          endLongitude: parseFloat(formData.endLongitude),
          maxHoursPerDay: parseInt(formData.maxHoursPerDay),
      };

      if (
        isNaN(dto.startLatitude) ||
        isNaN(dto.startLongitude) ||
        isNaN(dto.endLatitude) ||
        isNaN(dto.endLongitude) ||
        isNaN(dto.maxHoursPerDay)
      ) {
        setMessage("Error: All fields must be valid numbers.");
        return;
      }

      try {
        const response = await fetch(`${API_BASE_URL}/api/trip/generateTrip`, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${getToken()}`,
          },
          body: JSON.stringify(dto),
        });
  
        if (response.ok) {
          const newTrip = await response.json();
          onTripGenerated(newTrip);
          setFormData({
            startLatitude: "",
            startLongitude: "",
            endLatitude: "",
            endLongitude: "",
            maxHoursPerDay: "",
          });
          setMessage("Trip generated successfully!");
          setTimeout(() => {
            onClose();
            setMessage("");
          }, 1000); // 1 second
        } else {
          const errorData = await response.json()
          const error = errorData.error;
          setMessage(`Error: ${error}`);
        }
      } catch (error) {
        setMessage("Error: " + error.message);
      }
    };
  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>Generate Trip</DialogTitle>
      <DialogContent>
        <Box sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 1 }}>
          <TextField
            label="Start Latitude (-90 to 90)"
            value={formData.startLatitude}
            onChange={(e) => handleChange("startLatitude", e.target.value)}
            required
          />
          <TextField
            label="Start Longitude (-180 to 180)"
            value={formData.startLongitude}
            onChange={(e) => handleChange("startLongitude", e.target.value)}
            required
          />
          <TextField
            label="End Latitude (-90 to 90)"
            value={formData.endLatitude}
            onChange={(e) => handleChange("endLatitude", e.target.value)}
            required
          />
          <TextField
            label="End Longitude (-180 to 180)"
            value={formData.endLongitude}
            onChange={(e) => handleChange("endLongitude", e.target.value)}
            required
          />
          <TextField
            label="Max Travel Hours Per Day"
            value={formData.maxHoursPerDay}
            onChange={(e) => handleChange("maxHoursPerDay", e.target.value)}
            required
          />
          {message && (
            <Alert severity={message.startsWith("Error") ? "error" : "success"}>
              {message}
            </Alert>
          )}
        </Box>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button type="submit" variant="contained" color="primary" onClick={handleSubmit}>
          Generate
        </Button>
      </DialogActions>
    </Dialog>
  )
};

export default GenerateTripForm;
