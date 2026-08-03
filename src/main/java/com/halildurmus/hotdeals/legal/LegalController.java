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

    @GetMapping(value = "/eula", produces = "text/html")
    public String getEulaHtml() {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <title>Contrato de Licencia de Usuario Final (EULA) - Promoabastos</title>
                </head>
                <body>
                    <h1>CONTRATO DE LICENCIA DE USUARIO FINAL (EULA)</h1>
                    <p><em>Última actualización: 08-02-2026</em></p>
                    <p>El presente Contrato de Licencia de Usuario Final ("Contrato") es un acuerdo legal entre usted ("Usuario") y Promoabastos ("Licenciante", "nosotros" o "nuestro") para el uso de la aplicación móvil promoabastos ("Aplicación").</p>
                    <p>Al descargar, instalar o usar la Aplicación desde la Google Play Store, usted acepta los términos de este Contrato. Si no está de acuerdo, no descargue, instale ni use la Aplicación.</p>

                    <h2>1. CONCESIÓN DE LA LICENCIA</h2>
                    <p>Le otorgamos una licencia revocable, no exclusiva, no transferible y limitada para descargar, instalar y usar la Aplicación en dispositivos móviles de su propiedad, estrictamente de acuerdo con este Contrato y las condiciones de Google Play Store.</p>

                    <h2>2. CUENTAS DE USUARIO Y REGISTRO</h2>
                    <ul>
                        <li><strong>Registro:</strong> Para acceder a ciertas funciones, deberá crear una cuenta proporcionando datos personales exactos y actualizados.</li>
                        <li><strong>Seguridad:</strong> Usted es el único responsable de mantener la confidencialidad de sus credenciales de acceso.</li>
                        <li><strong>Uso no autorizado:</strong> Se compromete a notificarnos de inmediato cualquier uso no autorizado de su cuenta. No seremos responsables por pérdidas causadas por accesos no autorizados.</li>
                    </ul>

                    <h2>3. DATOS PERSONALES Y PRIVACIDAD</h2>
                    <ul>
                        <li><strong>Recopilación:</strong> Al registrarse y usar la Aplicación, usted reconoce que recopilamos datos personales (como nombre, correo electrónico y métricas de uso).</li>
                        <li><strong>Aviso de Privacidad:</strong> El tratamiento de sus datos personales se rige estrictamente por nuestro Aviso de Privacidad, disponible en <a href="/api/v1/legal/privacy-policy">Aviso de Privacidad</a>, de conformidad con la Ley Federal de Protección de Datos Personales en Posesión de los Particulares (LFPDPPP) en México.</li>
                    </ul>

                    <h2>4. COMPRAS INTEGRADAS Y SUSCRIPCIONES FUTURAS</h2>
                    <ul>
                        <li><strong>Funciones de Pago:</strong> La Aplicación puede ofrecer en el futuro funciones de pago, compras integradas o suscripciones ("Compras").</li>
                        <li><strong>Términos de Compra:</strong> Al implementar dichas funciones, los precios, métodos de pago y condiciones de facturación se mostrarán claramente antes de la compra y se procesarán de forma segura a través de la pasarela de pagos de Google Play Store.</li>
                        <li><strong>Modificaciones:</strong> Nos reservamos el derecho de modificar las tarifas del servicio futuro mediante un aviso previo dentro de la Aplicación.</li>
                    </ul>

                    <h2>5. RESTRICCIONES DE LA LICENCIA</h2>
                    <p>Usted acepta que NO realizará, ni permitirá que terceros realicen lo siguiente:</p>
                    <ul>
                        <li>Licenciar, vender, rentar, arrendar, asignar o distribuir la Aplicación.</li>
                        <li>Modificar, realizar obras derivadas, desensamblar, descompilar o realizar ingeniería inversa de cualquier parte de la Aplicación.</li>
                        <li>Utilizar la Aplicación para fines ilícitos o fraudulentos en territorio mexicano o internacional.</li>
                    </ul>

                    <h2>6. PROPIEDAD INTELECTUAL</h2>
                    <p>La Aplicación, incluyendo su código fuente, bases de datos, diseños, textos, gráficos y logotipos, son propiedad exclusiva de promoabastos y están protegidos por la Ley Federal del Derecho de Autor y la Ley Federal de Protección a la Propiedad Industrial en México.</p>

                    <h2>7. LIMITACIÓN DE RESPONSABILIDAD</h2>
                    <p>En la medida máxima permitida por las leyes mexicanas aplicables (incluyendo la Ley Federal de Protección al Consumidor), el Licenciante no será responsable por daños indirectos, incidentales o consecuentes (como pérdida de datos o fallas en el dispositivo) que deriven del uso o la imposibilidad de usar la Aplicación. La Aplicación se proporciona "TAL CUAL" ("AS IS").</p>

                    <h2>8. LEY APLICABLE Y JURISDICCIÓN</h2>
                    <p>Este Contrato se regirá e interpretará de conformidad con las leyes de los Estados Unidos Mexicanos. Para cualquier controversia, las partes se someten expresamente a la jurisdicción de los tribunales competentes de la Ciudad de México, renunciando a cualquier otro fuero que pudiera corresponderles por su domicilio presente o futuro.</p>

                    <h2>9. DATOS DE CONTACTO</h2>
                    <p>Para cualquier duda, aclaración o soporte técnico relacionado con la Aplicación, puede contactarnos en:</p>
                    <ul>
                        <li>Correo electrónico: soporte@promoabastos.com</li>
                        <li>Sitio web: promoabastos.com</li>
                    </ul>
                </body>
                </html>
                """;
    }
}
