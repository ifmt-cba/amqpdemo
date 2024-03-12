package br.edu.ifmt.amqpdemo.produtor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.RabbitConverterFuture;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    RabbitTemplate template;
    AsyncRabbitTemplate asyncTemplate;

    public MessageProducer(RabbitTemplate template) {
        this.template = template;
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(template.getConnectionFactory());
        container.setQueueNames("q.bob.reply");
        this.asyncTemplate = new AsyncRabbitTemplate(template, container);
        this.asyncTemplate.start();
    }

    public void enviarMensagemApenasParaAlice(String msg) {
        template.convertAndSend("de.message","rk.alice",msg);
    }

    public void enviarMensagemApenasParaBob(String msg) {
        template.convertAndSend("de.message","rk.bob",msg);
    }

    public void enviarMensagemApenasParaCarla(String msg) {
        template.convertAndSend("de.message","rk.carla",msg);
    }

    public void enviarMensagemParaTodos(String msg) {
        template.convertAndSend("fe.message","",msg);        
    }

    public void enviarMensagemParaMulheres(String msg) {
        template.convertAndSend("te.message","rk.naoimporta.mulher",msg);
    }

    public void enviarMensagemParaHomens(String msg) {
        template.convertAndSend("te.message","rk.naoimporta.homem",msg);
    }

    public void enviarMensagemParaMulheresAtivas(String msg) {
        MessageProperties propriedades = MessagePropertiesBuilder.newInstance()
                    .setHeader("sexo", "feminino")
                    .setHeader("situacao", "ativo")
                    .build();

        Message mensagem = new Message(msg.getBytes(),propriedades);

        template.convertAndSend("he.message","",mensagem);
    }
    
    public void enviarMensagemParaAposentados(String msg) {
        MessageProperties propriedades = MessagePropertiesBuilder.newInstance()
                    .setHeader("situacao", "aposentado")
                    .build();

        Message mensagem = new Message(msg.getBytes(),propriedades);

        template.convertAndSend("he.message","",mensagem);
    }

    public void enviarMensagemErrada(String msg) {
        template.convertAndSend("de.message","rk.daniel",msg);
    }

    public void enviarMensagemParaAliceComTempoDeVidaBaixo(String msg) {
        template.convertAndSend("de.message","rk.alice",msg,
            message -> {
                message.getMessageProperties().setExpiration(String.valueOf(20000));
                return message;
            });
            //Obs: mensagens vencidas que são entregues para um exchange são descartadas
            //     e não são encaminhadas para um dead letter exchange
    }

    public String descobrirQuemEh(String exchange) {
        return template.convertSendAndReceive(exchange,
                    "rk.quemsou",
                    "").toString();
    } 
    
    public String descobrirQuemEhDepois() throws InterruptedException, ExecutionException, TimeoutException {
        RabbitConverterFuture<String> future = asyncTemplate.convertSendAndReceive("de.message", "rk.bob.request", "");            
        //faço algum trabalho para tentar receber depois a resposta
        return future.get(10, TimeUnit.SECONDS);
    }

    public void descobrirQuemEhQuandoQuiserResponder() {
        RabbitConverterFuture<String> future = asyncTemplate.convertSendAndReceive("de.message", "rk.bob.request", "");
        future.whenComplete((result, exc) -> {
            try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
            if (exc == null)
                System.out.println("Recebi mensagem async de forma notificada: " +  result);
            else
                exc.printStackTrace();
        });
    }
}
