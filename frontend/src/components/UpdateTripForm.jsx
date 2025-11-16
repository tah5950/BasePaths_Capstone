import { useState, useEffect } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Typography,
  Button,
  Box,
  Alert,
} from "@mui/material";
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { getToken } from "../utils/authUtils";
import { API_BASE_URL } from "../config";

const UpdateTripForm = ({ open, onClose, onTripUpdated, trip })  => {
  const [message, setMessage] = useState("");
  const [formData, setFormData] = useState({
      name: "",
      startDate: null,
      endDate: null,
  });

  const handleChange = (key, value) => {
      setFormData((prev) => ({...prev, [key]: value}))
  };

  const handleSubmit = async (e) => {
      e.preventDefault();

      const payload = { 
            ...trip,
            name: formData.name,
            startDate: formData.startDate?.toISOString().split("T")[0],
            endDate: formData.endDate?.toISOString().split("T")[0],
        };
      
      try {
        const response = await fetch(`${API_BASE_URL}/api/trip/updateTrip`, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${getToken()}`,
          },
          body: JSON.stringify(payload),
        });
  
        if (response.ok) {
          const newTrip = await response.json();
          onTripUpdated(newTrip);
          setFormData({
              name: "",
              startDate: null,
              endDate: null,
          });
          setMessage("Trip updated successfully!");
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

  const parseDateOnly = (isoString) => {
    if (!isoString) return null;
    const [year, month, day] = isoString.split("T")[0].split("-").map(Number);
    return new Date(year, month - 1, day); // month is 0-indexed
  };

  useEffect(() => {
    if (trip && open) {
      setFormData({
        name: trip.name || "",
        startDate: parseDateOnly(trip.startDate),
        endDate: parseDateOnly(trip.endDate),
      });
    }
  }, [trip, open]);

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>Edit Trip</DialogTitle>
      <DialogContent>
        <Typography variant="subtitle2" color="error" sx={{ mb: 2 }}>
            Warning: Updating a trip's dates will remove trip stops and reset values used for generation.
        </Typography>
        <Box sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 1 }}>
          <TextField
            label="Trip Name"
            name="tripName"
            value={formData.name}
            onChange={(e) => handleChange("name", e.target.value)}
            required
          />
          <LocalizationProvider dateAdapter={AdapterDateFns}>
            <DatePicker
              label="Start Date"
              value={formData.startDate}
              onChange={(newValue) => handleChange("startDate", newValue)}
              slotProps={{ textField: { fullWidth: true, required: true, name: "startDate" } }}
            />
            <DatePicker
              label="End Date"
              name="endDate"
              value={formData.endDate}
              onChange={(newValue) => handleChange("endDate", newValue)}
              slotProps={{ textField: { fullWidth: true, required: true, name: "endDate" } }}
            /> 
          </LocalizationProvider>
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
          Update
        </Button>
      </DialogActions>
    </Dialog>
  )
};

export default UpdateTripForm;
