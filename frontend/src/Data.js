import { getMenuItems, getCategories } from "./Service";

export async function menuItemsData() {
    try {
        const response = await getMenuItems();
        return response.data;
    } catch (error) {
        console.log(error);
    }
}

export async function categoriesData() {
    try {
        const response = await getCategories();
        return response.data;
    } catch (error) {
        console.log(error);
    }
}