export default function DeleteGroupSelected(state = null, action) {
    switch (action.type) {
        case "DELETED_GROUP_SELECTED":
            return action.payload; 
        default:
            return state;
    }

}