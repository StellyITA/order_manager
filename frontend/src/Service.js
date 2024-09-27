import axios from 'axios';

const API_URL = "http://localhost:8080/";

export async function getMenuItems() {
    try {
        const response = await axios.get(API_URL + "menu");
        return response;
    } catch (err) {
        console.log(err);
    }
}

export async function getCategories() {
    try {
        const response = axios.get(API_URL + "menu/categories");
        return response;
    } catch (err) {
        console.log(err);
    }
}