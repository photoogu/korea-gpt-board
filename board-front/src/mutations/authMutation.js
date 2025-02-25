import { useMutation } from "@tanstack/react-query";
import { joinApi, loginApi } from "../apis/authApi";

export const useJoinMutation = () => useMutation({
    mutationKey: ["joinMutation"],
    mutationFn: joinApi,
    retry: 0, // join 에서는 retry 가 0 > 회원가입이 2번 될 수 있음(기본값이 1이기 때문)
});

export const useLoginMutation = () => useMutation({
    mutationKey: ["loginMutation"],
    mutationFn: loginApi,
    retry: 0,
});