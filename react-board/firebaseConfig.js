import firebase from "firebase/compat/app";
import "firebase/compat/database";

// //firebase 접속 정보
// // For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
    apiKey: "AIzaSyA4ppuTUmKq1Uf7H8GlhBvOpyZ_ayKGUCI",
    authDomain: "our-hobby-3eda3.firebaseapp.com",
    databaseURL: "https://our-hobby-3eda3-default-rtdb.asia-southeast1.firebasedatabase.app",
    projectId: "our-hobby-3eda3",
    storageBucket: "our-hobby-3eda3.firebasestorage.app",
    messagingSenderId: "435131149469",
    appId: "1:435131149469:web:34aa5a9be3515cf4073874",
    measurementId: "G-50MY29CMFQ"
  };

// Firebase 중복 초기화 방지
if (!firebase.apps.length) {
  firebase.initializeApp(firebaseConfig);
}

export const db = firebase.database();