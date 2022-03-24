package com.example.ifoodclone.api;

import com.example.ifoodclone.model.NotificacaoDados;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificacaoService {

    @Headers({
            "Authorization:key=AAAAjtMv4O4:APA91bFnsvWw1Jtf-lO-jCgHmjokqkJBadg6THPDpXns-4V2QBkngYlEA8ZeYMnagPizbAzA1fMu7hguRGdK6CWku9hsCRsSbTD776wcM_UO9l6Zl9kieuwPFwKGa8Qh-l6YC6uPwPIZ",
            "Content-Type:application/json"
    })
    @POST("send")
    Call<NotificacaoDados> salvar_notificacao(@Body NotificacaoDados notificacaoDados);

}
