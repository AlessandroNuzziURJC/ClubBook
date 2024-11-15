/**
 * NotificationTokenCheckDTO represents a Data Transfer Object (DTO) for checking notification tokens.
 */
class NotificationTokenCheckDTO {

    /**
     * Creates an instance of NotificationTokenCheckDTO.
     *
     * @param {string} deviceIdentifier - The unique identifier for the device.
     * @param {string} userId - The identifier of the user associated with the device.
     */
    constructor(deviceIdentifier, userId) {
        this.deviceIdentifier= deviceIdentifier;
        this.userId = userId;
    }
}

export default NotificationTokenCheckDTO;