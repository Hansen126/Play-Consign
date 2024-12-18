# 🚀 Play Consign

**Play Consign** is a mobile application designed to facilitate the buying and selling of **gaming gear**, both new and used, in a secure, fast, and user-friendly environment. This platform is specifically tailored for the **gaming community**, enabling users to find, sell, and purchase items such as keyboards, mice, headsets, and other gaming equipment effortlessly.

---

## 🎯 Key Features

- **🔑 User Registration & Authentication**  
  Secure user registration and login using **Firebase Authentication**.

- **📦 Seller Registration & Product Upload**  
  Sellers can upload products with details like name, price, category, condition, description, and image.

- **🔍 Search & Filter**  
  Easily search for products based on **keywords** or **categories**.

- **💵 Consign Product Flow**  
  Sellers pay a small fee of **IDR 5,000** via **Midtrans Payment Gateway** after submitting product details. Successful payment adds the product to the database for listing.

- **📱 Contact Seller**  
  Buyers can contact sellers directly via **WhatsApp** for negotiation or inquiries.

- **📂 Category-Based Browsing**  
  Browse products by categories like Mouse, Keyboard, PC Parts, Consoles, Monitors, and more.

- **✨ Responsive User Interface**  
  Clean and responsive UI for smooth navigation.

---

## ⚙️ Technologies Used

- **🛠️ Development Tools**: [Android Studio](https://developer.android.com/studio)  
- **👨‍💻 Programming Language**: Java  
- **📋 Database**: Firebase Realtime Database  
- **🔐 Authentication**: Firebase Authentication  
- **🖼️ Image Management**: [Cloudinary API](https://cloudinary.com/)  
- **💳 Payment Gateway**: [Midtrans Snap API](https://midtrans.com/)  

---

## 🛠️ Setup and Installation

Follow these steps to set up **Play Consign** on your local environment:

1. **Clone the repository**:  
   ```bash
   git clone https://github.com/Hansen126/Play-Consign.git
   cd Play-Consign

2. **🔗 Bind the project to your Firebase account**:  
   - Go to the [Firebase Console](https://console.firebase.google.com/) and create a new project.  
   - Download the `google-services.json` file.  
   - Place the `google-services.json` file inside the `app/` directory of the project.

3. **🖼️ Add your Cloudinary API Key**:  
   - Sign up for a [Cloudinary](https://cloudinary.com/) account if you don’t already have one.  
   - Open the `gradle.properties` file in the root directory and add:  
     ```properties
     CLOUDINARY_CLOUD_NAME=<your_cloudinary_cloud_name>
     CLOUDINARY_API_KEY=<your_cloudinary_api_key>
     CLOUDINARY_API_SECRET=<your_cloudinary_api_secret>
     ```

4. **💻 Set up a backend server for Midtrans**:  
   - The backend server is required to securely process the Midtrans server key.  
   - **Option 1: Use your own server**:  
     - Set up a simple **Node.js backend** to handle Midtrans API requests.  
     - Add your Midtrans server key to your backend configuration.  
   - **Option 2: Use the pre-configured backend server**:  
     - You can use the **base URL** of the backend server that is already deployed on **Railway** (included in the project code).  
     - No further backend setup is needed.

5. **⚙️ Sync and Run the Project**:  
   - Open the project in **Android Studio**.  
   - Sync Gradle files.  
   - Build and run the project on an **Android Emulator** or a physical device.

---

## 📲 Experience It Yourself  

Download the APK to try the application:  
[**Play Consign APK**](https://drive.google.com/file/d/1YUEIF6YRek6nKGLI83OoCanvL7gc_8Tf/view?usp=sharing)

---

## 👨‍💻 Contribution

Contributions are welcome! Open an **issue** or submit a **pull request** to improve the project. 🚀

---


## 📞 Contact

For further inquiries, feel free to reach out:  
- **GitHub**: [Hansen126](https://github.com/Hansen126)

---

Enjoy working on **Play Consign**! 🎮✨
