import {PREFIX_API_LOGIN} from "@/constants/url";
import request from "@/services/request";
import type {DefaultResponse} from "@/utils/types/api.common";
import type {AxiosResponse} from "axios";

export interface LoginForm {
    taiKhoan: string;
    matKhau: string;
}


export const login = async (
    params: LoginForm
) => {
    try {
        const res = (await request({
            url: `${PREFIX_API_LOGIN}`,
            method: "POST",
            data: params,
        })) as AxiosResponse<DefaultResponse<String>>;

        return res.data;
    } catch (error) {
        return error as AxiosResponse<DefaultResponse<any>>;
    }
};

