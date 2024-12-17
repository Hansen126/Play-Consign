require('dotenv').config();
const express = require('express');
const cors = require('cors');
const midtransClient = require('midtrans-client');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());
app.use(cors());

// Inisialisasi Midtrans
const snap = new midtransClient.Snap({
    isProduction: false, // Ganti ke true untuk Production
    serverKey: process.env.MIDTRANS_SERVER_KEY
});

console.log("PORT:", process.env.PORT);
console.log("MIDTRANS_SERVER_KEY:", process.env.MIDTRANS_SERVER_KEY);

// Endpoint untuk membuat Snap Token
app.post('/create-snap-token', (req, res) => {
    const { orderId, amount, name, email, phone } = req.body;
    
    // Log parameter yang diterima
    console.log("Order ID:", orderId);
    console.log("Gross Amount:", amount);
    console.log("Customer Name:", name);
    console.log("Email:", email);
    console.log("Phone:", phone);

    // Parameter transaksi Midtrans
    const parameter = {
        transaction_details: {
            order_id: orderId,
            gross_amount: amount 
        },
        customer_details: {
            first_name: name,
            email: email,
            phone: phone
        }
    };

    // Membuat Snap Token
    snap.createTransaction(parameter)
        .then((transaction) => {
            console.log("Snap Token:", transaction.token);
            res.status(200).json({ snapToken: transaction.token });
        })
        .catch((error) => {
            console.error("Error creating Snap Token:", error.message);
            res.status(500).json({ error: error.message });
        });
});

// Jalankan server
app.listen(PORT, () => {
    console.log(`Server berjalan di http://localhost:${PORT}`);
});

app.get('/', (req, res) => {
    res.send("Server Midtrans berjalan dengan baik! 🚀");
});

