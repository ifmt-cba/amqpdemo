package br.edu.ifmt.amqpdemo.consumidor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class Bob {

    @RabbitListener(queues = "q.bob")
    private void recebeMensagem(String msg) throws InterruptedException {
        Thread.sleep(8000);
        System.out.println("Bob recebeu: " + msg);
    }

    @RabbitListener(queues = "q.bob.request")
    private String quemSou() throws InterruptedException {
        Thread.sleep(3000);
        return "Olá, me chamo Bob!";
    }

}
