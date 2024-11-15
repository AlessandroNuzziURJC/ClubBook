/**
 * NotificationTokenData represents a Data Transfer Object (DTO) for notification token information.
 */
class NotificationTokenData {

    /**
     * Creates an instance of NotificationTokenData.
     *
     * @param {string} deviceIdentifier - The unique identifier for the device.
     * @param {string} token - The notification token associated with the device.
     * @param {string} userId - The identifier of the user associated with the device.
     */
    constructor(deviceIdentifier, token, userId) {
        this.deviceIdentifier = deviceIdentifier;
        this.token = token;
        this.userId = userId;
    }
}

export default NotificationTokenData;