package com.halildurmus.hotdeals.legal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/legal")
public class LegalController {

    @GetMapping
    public Map<String, String> getPrivacyPolicy() {
        Map<String, String> response = new HashMap<>();
        response.put("type", "privacy-policy");
        response.put("version", "1.0");
        response.put("lastUpdated", "2026-05-12");
        response.put("content", "Your full privacy policy text goes here...");
        return response;
    }

    @GetMapping(value = "/privacy-policy", produces = "text/html")
    public String getLegalHtml() {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <title>Política de Privacidad - Promoabastos</title>
                </head>
                <body>
                    <p><em>Última actualización: 08-02-2026</em></p>
                    <h1>1. Sobre nuestra Política de Privacidad</h1>
                    <p>Esta Política de privacidad se aplica a la Información personal que Promodescuentos recopila de ti o sobre ti cuando visitas nuestra Plataforma o utilizas nuestros Servicios. A continuación explicamos por qué y cómo procesamos su Información Personal.</p>
                    <p>Responsable del procesamiento de datos dentro del alcance de este sitio web:<br>Promoabastos</p>
                    <p>Es posible que hagamos cambios a esta Política de Privacidad, particularmente si hay cambios en la legislación. La versión actualizada de la Política de Privacidad estaría disponible de forma automática en esta página.</p>

                    <h2>2. Definiciones</h2>
                    <p>“Servicios” significa todos los servicios en línea que ofrecemos cuando accedes a o usas nuestra Plataforma</p>
                    <p>“Plataforma” significa nuestra aplicación móvil o nuestro sitio web al que puedes acceder en la siguiente dirección: promodescuentos.com.</p>
                    <p>"Información Personal" significa cualquier información relacionada a ti sobre la base de la cual puedes ser identificado, directa o indirectamente.</p>

                    <h2>3. ¿Cómo puedes contactarnos?</h2>
                    <p>Hemos designado un oficial de protección de datos al que puedes contactar vía privacy@promoabastos.com</p>

                    <h2>4. ¿Qué tipo de Información Personal concerniente a ti procesamos?</h2>
                    <p>Procesamos diferentes tipos de Información Personal:</p>
                    <ul>
                        <li>Información Personal que debes proporcionar: dependiendo de tu uso de nuestros Servicios, necesitaremos que nos proporciones algunas categorías de Información Personal. Información de la cuenta (dirección email, nombre de usuario, contraseña)</li>
                        <li>Información Personal que podrías proporcionar: cuando usas nuestros Servicios, es posible que proporciones categorías adicionales de Información Personal. Información adicional (nombre, apellido, tu descripción)</li>
                        <li>Tu actividad (tus ofertas, discusiones, comentarios, publicaciones guardadas, "me gusta", alertas, filtros, búsquedas, mensajes privados)</li>
                        <li>Tus respuestas a nuestras encuestas (dirección de email y datos de comportamiento)</li>
                        <li>Información Personal que recopilamos automáticamente: también recopilamos ciertas categorías de Información Personal cuando visitas nuestra Plataforma o usas nuestros Servicios. Tus tendencias (me gusta recibidos, seguidores, estadísticas sobre tus ofertas)</li>
                        <li>Datos de comportamiento (páginas vistas, clics, duración de datos, tus me gusta, filtros activos, alertas, búsquedas)</li>
                        <li>Datos de registro e información del dispositivo (hora, fecha, dirección de IP)</li>
                        <li>Información Personal que recopilamos de un socio de negocios (con tu consentimiento): cuando nos das consentimiento para ello (por ejemplo cuando aceptas cookies de un tercero en nuestra Plataforma o en el sitio web de un tercero), también recolectamos algunas categorías de Información Personal de socios de negocios (comercializadores, anunciantes, vendedores). Información de la cuenta (dirección de email, nombre de usuario)</li>
                        <li>Número de teléfono</li>
                        <li>Datos de la orden</li>
                        <li>Tus respuesta a nuestras encuestas (dirección de email y datos de comportamiento)</li>
                        <li>Datos de comportamiento (página vista, tiempo en la Plataforma, clics, interacciones con la Pltaforma...)</li>
                        <li>Datos de registro e información del dispositivo (hora, fecha, dirección IP anonimizada, geo-localización, información de navegador, tamaño de la pantalla, sistema opertavio, ID de dispositivo, ID de publicidad)</li>
                    </ul>

                    <h2>5. ¿Por qué es necesaria parte de tu Información Personal?</h2>
                    <p>En algunas ocasiones necesitamos que nos proporciones parte de tu Información Personal. Lo solicitamos cuando es necesario para proporcionar nuestors Servicios o es requerido por la ley. Por ejemplo, necesitamos tu dirección de email cuando te registras a nuestro boletín o cuando creas una cuenta en nuestra Plataforma.<br>No proporcionar esta información necesaria puede significa no podremos crear tu cuenta, no podremos proporcionarte nuestros servicios, etc.</p>

                    <h2>6. ¿Por qué y para qué usamos tu Información Personal?</h2>
                    <p>Necesitamos usar tu Información Personal por varias razones, que pueden ser:</p>
                    <ul>
                        <li>La necesidad de entrar en o realizar Nuestros Términos de Uso contigo (necesidad contractual),</li>
                        <li>Nuestra necesidad de cumplir con las obligaciones legales (cumplimiento de la ley),</li>
                        <li>Un interés legítimo que tenemos en el procesamiento de tu Información Personal (nuestro Interés Legítimo),</li>
                        <li>Has consentido a dicho procesamiento (tu Consentimiento)</li>
                    </ul>
                    <p><strong>TEN EN CUENTA QUE TIENES EL DERECHO DE RETIRAR TU CONSENMIENTO U OBJETAR EL PROCESAMIENTO BASADO EN TU INTERÉS LEGÍTIMO PARA CUALQUIER PROCESAMIENTO DE LAS OPERACIONES LISTADAS ARRIBA EN CUALQUIER MOMENTO, CONTACTÁNDONOS O RETIRÁNDOTE Y RETIRANDO TU INFORMACIÓN PERSONAL DE LA PLATAFORMA, CUANDO SEA POSIBLE</strong></p>
                    <p>Recopilamos, procesamos y usamos tu Información Personal arriba mencionada con los siguientes propósitos:</p>
                    <table border="1">
                        <tr>
                            <th>PROPÓSITOS</th>
                            <th>BASE LEGAL</th>
                            <th>(CATEGORÍAS DE) INFORMACIÓN PERSONAL CONCERNIENTE</th>
                        </tr>
                        <tr>
                            <td>Registrarse e iniciar sesión en nuestra Plataforma</td>
                            <td>Necesidad contractual</td>
                            <td>Información de la cuenta</td>
                        </tr>
                        <tr>
                            <td>Ofrecer nuestros servicios</td>
                            <td>Necesidad contractual</td>
                            <td>Información de la cuenta, Información adicional, Tu actividad, Tus tendencias.</td>
                        </tr>
                    </table>

                    <h2>7. ¿Por cuánto tiempo retenemos tu Información Personal?</h2>
                    <p>Conservamos tu Información Personal por el tiempo que es necesario de acuerdo a los propósitos para los cuales se obtuvo, notablemente por el tiempo que uses nuestros Servicios y por un periodo de 3 (tres) años a partir de nuestro último contacto contigo o el último uso de dichos Servicios en el caso que hayas creado una cuenta en nuestra Pataforma.<br>Al terminar dicho periodo, estaremos en la posibilidad de contactarte para saber si quieres continuar usando nuestros Servicios. En la ausencia de una respuesta positiva y explícita de tu parte, tus datos personales serán borrados o archivados de acuerdo con las provisiones vigentes.</p>
                    <p>Por favor ten en cuenta que aún si tu cuenta e Información Personal son borradas:</p>
                    <ul>
                        <li>Tus contribuciones, publicaciones, ofertas y comentarios serán conservados de forma anónima</li>
                        <li>Tus mensajes privados permancerán siempre disponibles para los receptores de dichos mensajes</li>
                        <li>Si tu cuenta es bloqueada, conservaremos parte de tu Información Personal por un periodo de entre 1 y 3 años para prevenir que violes las reglas que aplican en nuestra Plataforma</li>
                        <li>Conservamos la información de cookies por un periodo de 30 días a partir de que fueron recopiladas, en el evento que no hayas inciado sesión en nuestra Plataforma, y por hasta 13 meses en otras instancias.</li>
                    </ul>

                    <h2>8. ¿Quiénes reciben tu Información Personal?</h2>
                    <p>No vendemos tus datos personales a terceros.</p>
                </body>
                </html>
                """;
    }
}
