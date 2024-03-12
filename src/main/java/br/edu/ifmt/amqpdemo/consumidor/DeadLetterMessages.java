package br.edu.ifmt.amqpdemo.consumidor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeadLetterMessages {

    @RabbitListener(queues = "q.deadletter")
    private void recebeMensagem(String msg) throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("Mensagem vencida: " + msg);
    }

}
