import axios from 'axios';
import { getServerUrl } from '../../config/env';

const API_URL = getServerUrl();

class PasswordService {
    async requestPasswordReset(email) {
        try {
            const response = await axios.post(`${API_URL}/forgotPassword/v2`, { email });
            return { success: true, message: response.data.message };
        } catch (error) {
            return { 
                success: false, 
                message: error.response?.data?.message || 'Failed to send reset email' 
            };
        }
    }

    async validateResetToken(token) {
        try {
            const response = await axios.get(`${API_URL}/resetPassword/v2/validate`, {
                params: { token }
            });
            return response.data; // {valid: true/false, message: "..."}
        } catch (error) {
            return { valid: false, message: 'Error validating token' };
        }
    }

    async resetPassword(token, newPassword) {
        try {
            const response = await axios.post(`${API_URL}/resetPassword/v2`, {
                token,
                newPassword
            });
            return { success: true, message: response.data.message };
        } catch (error) {
            return { 
                success: false, 
                message: error.response?.data?.message || 'Failed to reset password' 
            };
        }
    }
}

export default new PasswordService();
