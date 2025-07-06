import {PREFIX_API_LOGIN, PREFIX_API_LOGIN_ADMIN} from "@/constants/url";
import request from "@/services/request";
import type {DefaultResponse} from "@/utils/types/api.common";
import type {AxiosResponse} from "axios";

export interface LoginForm {
    email: string;
    password: string;
}

export interface LoginResponseData {
  accessToken: string
  refreshToken: string
}


export const loginUser = async (
    params: LoginForm
) => {
    try {
        const res = (await request({
            url: `${PREFIX_API_LOGIN}`,
            method: "POST",
            data: params,
        })) as AxiosResponse<DefaultResponse<LoginResponseData>>;

        return res.data;
    } catch (error) {
        return error as AxiosResponse<DefaultResponse<any>>;
    }
};


export const loginAdmin = async (
    params: LoginForm
) => {
    try {
        const res = (await request({
            url: `${PREFIX_API_LOGIN_ADMIN}`,
            method: "POST",
            data: params,
        })) as AxiosResponse<DefaultResponse<LoginResponseData>>;

        return res.data;
    } catch (error) {
        return error as AxiosResponse<DefaultResponse<any>>;
    }
};