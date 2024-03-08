package br.edu.ifmt.amqpdemo.consumidor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class Bob {

    @RabbitListener(queues = "q.bob")
    private void recebeMensagem(String msg) {
        System.out.println("Bob recebeu: " + msg);
    }

}
