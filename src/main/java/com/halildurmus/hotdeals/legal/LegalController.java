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
        response.put("lastUpdated", "2026-09-12");
        response.put("content", "Your full privacy policy text goes here...");
        return response;
    }

    @GetMapping(value = "/privacy-policy", produces = "text/html")
    public String getLegalHtml() {
        return """
                <!DOCTYPE html>
                <html>

                <head>
                      <meta charset="utf-8">
                      <meta name="viewport" content="width=device-width, initial-scale=1.0">
                      <title>Welcome file</title>
                </head>

                <body>
                      <h1 id="aviso-de-privacidad---promoabastos">AVISO DE PRIVACIDAD - PROMOABASTOS</h1>
                      <p><strong>Última actualización:</strong> 12 de septiembre de 2026</p>
                      <p>Este Aviso de Privacidad regula el tratamiento de la Información Personal que Promoabastos recopila de usted
                            cuando interactúa con nuestra Plataforma o utiliza nuestros Servicios. A continuación, detallamos la
                            naturaleza, propósito y alcance del procesamiento de su Información Personal.</p>
                      <p>El responsable del procesamiento de datos es Promoabastos.</p>
                      <p>Promoabastos se reserva el derecho de modificar el presente Aviso de Privacidad en cualquier momento, de
                            conformidad con las actualizaciones normativas o cambios operativos en nuestros servicios. Toda versión
                            modificada será publicada y entrará en vigor inmediatamente a través de esta página.</p>
                      <h2 id="definiciones">1. DEFINICIONES</h2>
                      <p><strong>1.1. “Servicios”:</strong> Todos los servicios en línea, funciones y características que ofrecemos
                            cuando el usuario accede a, o utiliza, nuestra Plataforma.</p>
                      <p><strong>1.2. “Plataforma”:</strong> Nuestra aplicación móvil y nuestro sitio web, accesibles a través del
                            dominio <a href="http://promoabastos.com">promoabastos.com</a> y tiendas de aplicaciones autorizadas.</p>
                      <p><strong>1.3. “Información Personal”:</strong> Cualquier información relacionada a una persona física
                            identificada o identificable.</p>
                      <p><strong>1.4. “EULA” (Contrato de Licencia de Usuario Final):</strong> El acuerdo legal vinculante entre
                            Promoabastos y el usuario que establece los términos y condiciones de uso de la Plataforma.</p>
                      <p><strong>1.5. “Contenido Generado por el Usuario (UGC)”:</strong> Toda la información, ofertas, comentarios,
                            imágenes, y cualquier otro material publicado por los usuarios en la Plataforma.</p>
                      <p><strong>1.6. “Oferta”:</strong> Información concerniente a descuentos, precios o disponibilidad de productos en
                            establecimientos, aportada por la comunidad de usuarios.</p>
                      <h2 id="datos-de-contacto">2. DATOS DE CONTACTO</h2>
                      <p><strong>2.1.</strong> Hemos designado a un Responsable de Protección de Datos, con quien usted puede
                            comunicarse para ejercer sus derechos o resolver dudas respecto al manejo de su información.</p>
                      <p><strong>2.2. Vías de contacto:</strong> A través del correo electrónico <a
                                  href="mailto:privacy@promoabastos.com">privacy@promoabastos.com</a>, o mediante cualquier otro canal
                            de contacto oficial proporcionado dentro de la Plataforma (p. ej., funciones de soporte integrado) o
                            comunicado directamente al usuario.</p>
                      <h2 id="categorías-de-información-personal-recopilada">3. CATEGORÍAS DE INFORMACIÓN PERSONAL RECOPILADA</h2>
                      <p>Recopilamos distintas categorías de información dependiendo del grado de su interacción con nuestra Plataforma:
                      </p>
                      <p><strong>3.1. Información proporcionada directamente por el usuario:</strong></p>
                      <ul>
                            <li>
                                  <p><strong>3.1.1. Datos de cuenta:</strong> Dirección de correo electrónico, nombre de usuario y
                                        contraseñas necesarias para formalizar el registro.</p>
                            </li>
                            <li>
                                  <p><strong>3.1.2. Datos de perfil (opcionales):</strong> Nombre, apellidos, número telefónico y
                                        descripción biográfica.</p>
                            </li>
                            <li>
                                  <p><strong>3.1.3. Contenido generado e interacciones:</strong> Ofertas publicadas, comentarios,
                                        mensajes directos, publicaciones guardadas, interacciones (“me gusta”), filtros y alertas de
                                        búsqueda configurados.</p>
                            </li>
                            <li>
                                  <p><strong>3.1.4. Encuestas:</strong> Dirección de correo electrónico y opiniones emitidas
                                        voluntariamente.</p>
                            </li>
                      </ul>
                      <p><strong>3.2. Información recopilada automáticamente:</strong></p>
                      <ul>
                            <li>
                                  <p><strong>3.2.1. Datos de comportamiento y uso:</strong> Registro de páginas visualizadas, clics,
                                        duración de sesión, interacción con ofertas y métricas generales de navegación.</p>
                            </li>
                            <li>
                                  <p><strong>3.2.2. Datos técnicos y del dispositivo:</strong> Fecha y hora de acceso, dirección IP (y
                                        su versión anonimizada), características del navegador, sistema operativo, resolución de
                                        pantalla, e identificadores de dispositivo (Device ID / Advertising ID).</p>
                            </li>
                            <li>
                                  <p><strong>3.2.3. Datos de ubicación:</strong> Su información de geo-localización, recopilada
                                        estrictamente bajo su consentimiento previo.</p>
                            </li>
                      </ul>
                      <p><strong>3.3. Información recopilada a través de terceros:</strong></p>
                      <ul>
                            <li><strong>3.3.1.</strong> Datos básicos de perfil (correo electrónico y nombre de usuario) provenientes de
                                  proveedores de identidad de terceros, en caso de que usted decida registrarse o iniciar sesión
                                  utilizando dichos servicios.</li>
                      </ul>
                      <h2 id="necesidad-del-tratamiento-de-datos">4. NECESIDAD DEL TRATAMIENTO DE DATOS</h2>
                      <p><strong>4.1.</strong> La provisión de cierta Información Personal es indispensable para formalizar la relación
                            jurídica y habilitar el uso de los Servicios. La negativa a proveer datos mínimos (como el correo
                            electrónico) imposibilitará la creación de una cuenta y la consecuente prestación del servicio.</p>
                      <h2 id="fundamentos-y-finalidades-del-procesamiento-de-datos">5. FUNDAMENTOS Y FINALIDADES DEL PROCESAMIENTO DE
                            DATOS</h2>
                      <p>Procesamos su Información Personal bajo los siguientes fundamentos legales:</p>
                      <ul>
                            <li>
                                  <p><strong>5.1. Necesidad contractual:</strong> Para dar cumplimiento a nuestras obligaciones emanadas
                                        del EULA.</p>
                            </li>
                            <li>
                                  <p><strong>5.2. Cumplimiento legal:</strong> Para acatar disposiciones legales o requerimientos de
                                        autoridades competentes.</p>
                            </li>
                            <li>
                                  <p><strong>5.3. Interés legítimo:</strong> Para optimizar la Plataforma, prevenir fraudes y garantizar
                                        la seguridad de nuestra infraestructura y comunidad.</p>
                            </li>
                            <li>
                                  <p><strong>5.4. Consentimiento expreso:</strong> Para fines específicos que requieran su autorización
                                        explícita, como el procesamiento de datos de geo-localización.</p>
                            </li>
                      </ul>
                      <p><strong>5.5. Uso de Datos de Ubicación:</strong></p>
                      <p>La información de geo-localización se procesa de forma exclusiva para la operatividad central de los servicios
                            de la Plataforma, permitiendo la visualización de la posición del usuario en el mapa interactivo y la
                            optimización de rutas hacia ofertas cercanas.</p>
                      <p><strong>5.6. Uso Limitado de Datos de Google API (Cumplimiento de Políticas):</strong></p>
                      <p>El uso y la transferencia a terceros de cualquier información proveniente de las API de Google se someterá de
                            forma estricta a la política <em>Google API Services User Data Policy</em>.</p>
                      <ul>
                            <li>
                                  <p><strong>5.6.1.</strong> Limitamos el uso de los datos obtenidos de las API de Google de manera
                                        exclusiva a la provisión o mejora de las funcionalidades dirigidas al usuario final de nuestra
                                        aplicación.</p>
                            </li>
                            <li>
                                  <p><strong>5.6.2.</strong> No realizamos de ninguna manera, el uso o transferencia de datos obtenidos
                                        de las API de Google para: publicidad dirigida, venta a intermediarios de datos (data brokers),
                                        determinación de riesgo crediticio, creación generalizada de bases de datos, o desarrollo y
                                        entrenamiento de modelos de inteligencia artificial (IA) o aprendizaje automático (ML). Esta
                                        restricción aplica de manera exclusiva a los datos obtenidos a través de los alcances (scopes)
                                        de la API de Google.</p>
                            </li>
                      </ul>
                      <p><strong>5.7. Datos Agregados y Tendencias de Mercado:</strong></p>
                      <p>Promoabastos se reserva el derecho de anonimizar y agregar información de comportamiento y datos factuales (p.
                            ej., reportes de precios en el mercado) para la elaboración de estadísticas, inteligencia de negocio y
                            gráficos de tendencias históricas. Toda vez que esta información agregada ha sido disociada y no constituye
                            Información Personal identificable, Promoabastos ostenta el derecho de utilizarla, licenciarla o
                            comercializarla libremente con terceros para propósitos de investigación, servicios de API o inteligencia de
                            mercado.</p>
                      <p><em>EL USUARIO TIENE EL DERECHO DE REVOCAR SU CONSENTIMIENTO U OPONERSE AL PROCESAMIENTO DE SUS DATOS EN
                                  CUALQUIER MOMENTO MEDIANTE CONTACTO DIRECTO O ELIMINACIÓN DEFINITIVA DE SU CUENTA.</em></p>
                      <h2 id="política-de-retención-de-datos">6. POLÍTICA DE RETENCIÓN DE DATOS</h2>
                      <p><strong>6.1. Plazos de Conservación:</strong> Conservaremos su Información Personal por el periodo
                            estrictamente necesario para cumplir con los fines expuestos en el presente documento, y hasta por un plazo
                            máximo de 3 (tres) años contados a partir de su última interacción con la Plataforma.</p>
                      <p><strong>6.2. Eliminación de Cuenta:</strong> Al solicitar el usuario la eliminación de su cuenta:</p>
                      <ul>
                            <li>
                                  <p><strong>6.2.1.</strong> El UGC (ofertas, comentarios, etc.) será disociado de su identidad y
                                        conservado de forma <strong>anónima</strong>.</p>
                            </li>
                            <li>
                                  <p><strong>6.2.2.</strong> La información factual e histórica (precios reportados, ubicaciones) será
                                        conservada de forma independiente y permanente para fines operativos, al no estar sujeta a
                                        derechos de autor y no constituir Información Personal.</p>
                            </li>
                            <li>
                                  <p><strong>6.2.3.</strong> Las comunicaciones privadas entre usuarios permanecerán accesibles
                                        exclusivamente para los receptores originales de dichos mensajes.</p>
                            </li>
                      </ul>
                      <p><strong>6.3. Sanciones y Prevención:</strong> En caso de que una cuenta sea suspendida por violaciones al EULA,
            se conservará la Información Personal vinculada a la misma por un periodo de entre 1 (uno) y 3 (tres) años,
            con el fin exclusivo de prevenir la evasión de la sanción y salvaguardar la integridad de la comunidad.</p>
                      <h2 id="transferencia-y-compartición-de-datos-a-terceros">7. TRANSFERENCIA Y COMPARTICIÓN DE DATOS A TERCEROS</h2>
                      <p><strong>7.1. Prohibición de Venta de Datos:</strong> Promoabastos no comercializa ni vende su Información
            Personal a terceros.</p>
                      <p><strong>7.2. Visibilidad Pública y Distribución Externa:</strong> Toda la información de perfil o de contacto
            que usted decida configurar explícitamente como pública, así como las ofertas o publicaciones que genere,
            será visible y compartida con el resto de los usuarios de la Plataforma. Asimismo, usted reconoce y acepta
            que las publicaciones pueden ser compartidas fuera de la aplicación (por ejemplo, mediante enlaces
            compartibles), por lo que dicho contenido público podría ser visualizado e indexado por terceros y personas
            no registradas en la Plataforma.</p>
                      <p><strong>7.3. Proveedores de Servicios y Operación Comercial:</strong> Promoabastos podrá compartir cierta
            información (estrictamente aquella exenta de las restricciones de la API de Google) con terceros proveedores
            de servicios técnicos, analítica o redes publicitarias (ej. Google Ads), con el objetivo exclusivo de
            asegurar la operación técnica, seguridad y la viabilidad económica de la Plataforma, siempre dentro del
            marco permitido por la legislación vigente.</p>

                </body>

                </html>
                """;
    }

    @GetMapping(value = "/eula", produces = "text/html")
    public String getEulaHtml() {
        return """
                <!DOCTYPE html>
                <html>

                <head>
                      <meta charset="utf-8">
                      <meta name="viewport" content="width=device-width, initial-scale=1.0">
                      <title>Welcome file</title>
                </head>

                <body>
                      <h1 id="contrato-de-licencia-de-usuario-final-eula">CONTRATO DE LICENCIA DE USUARIO FINAL (EULA)</h1>
                      <p><strong>Última actualización:</strong> 13 de septiembre de 2026</p>
                      <p>El presente Contrato de Licencia de Usuario Final (en lo sucesivo, el “Contrato”) constituye un acuerdo legal
                            vinculante entre usted (el “Usuario”) y Promoabastos (el “Licenciante” o “Nosotros”) que rige su acceso y
                            uso de la aplicación móvil y servicios asociados (conjuntamente, la “Aplicación” o “Plataforma”).</p>
                      <p>La instalación, descarga o utilización continua de la Aplicación implica la aceptación expresa, plena y sin
                            reservas de los términos de este Contrato.</p>
                      <h2 id="concesión-de-la-licencia">1. CONCESIÓN DE LA LICENCIA</h2>
                      <p><strong>1.1. Licencia Limitada:</strong> Sujeto a su cumplimiento continuo de este Contrato, el Licenciante
                            otorga al Usuario una licencia personal, revocable, no exclusiva, no sublicenciable y no transferible para
                            instalar y utilizar la Aplicación en sus dispositivos móviles, de conformidad con las políticas y
                            condiciones de las plataformas de distribución de aplicaciones correspondientes.</p>
                      <h2 id="cuentas-de-usuario-y-seguridad">2. CUENTAS DE USUARIO Y SEGURIDAD</h2>
                      <p><strong>2.1. Obligación de Registro:</strong> El acceso a la totalidad de las funcionalidades de la Plataforma
                            requiere la creación de una cuenta, para lo cual el Usuario deberá proporcionar información veraz, exacta,
                            actualizada y completa.</p>
                      <p><strong>2.2. Seguridad de Credenciales:</strong> El Usuario asume la responsabilidad exclusiva de salvaguardar
                            la confidencialidad de sus credenciales de acceso y de cualquier actividad u operación efectuada bajo su
                            cuenta.</p>
                      <p><strong>2.3. Suspensión y Revocación Discrecional:</strong> Promoabastos se reserva el derecho unilateral e
                            inobjetable de suspender, restringir o revocar cuentas de Usuario de forma inmediata, sin responsabilidad ni
                            requerimiento de notificación previa, cuando se detecten violaciones al presente Contrato, conductas
                            perjudiciales para la comunidad o riesgos de seguridad operativa dictaminados por el equipo de
                            administración de Promoabastos.</p>
                      <h2 id="datos-personales-y-privacidad">3. DATOS PERSONALES Y PRIVACIDAD</h2>
                      <p><strong>3.1. Tratamiento de Datos:</strong> La recopilación, el procesamiento y la salvaguarda de su
                            Información Personal se somete íntegramente a lo estipulado en nuestro <a
                                  href="https://api.promoabastos.com/api/v1/legal/privacy-policy">Aviso de Privacidad</a>. El uso de la
                            Plataforma constituye su reconocimiento y aceptación del tratamiento de datos conforme a dicho aviso y a la
                            legislación aplicable.</p>
                      <h2 id="contenido-generado-por-el-usuario-ugc">4. CONTENIDO GENERADO POR EL USUARIO (UGC)</h2>
                      <p><strong>4.1. Titularidad y Garantías Legales:</strong> El Usuario declara y garantiza de manera expresa que
                            posee o cuenta con las licencias, derechos, consentimientos y permisos legales pertinentes para publicar
                            cualquier contenido, información u oferta (conjuntamente, el “UGC”) en la Plataforma.</p>
                      <p><strong>4.2. Veracidad de la Información:</strong> El Usuario se obliga a proporcionar información exacta, real
                            y no engañosa. Promoabastos no avala ni garantiza la exactitud, integridad o fiabilidad del contenido
                            publicado por la comunidad de usuarios.</p>
                      <p><strong>4.3. Moderación y Eliminación de Contenido:</strong> Promoabastos ostenta la facultad discrecional de
                            monitorear, editar, rechazar o eliminar de manera inmediata y sin previo aviso el UGC que, a su exclusivo
                            criterio, infrinja este Contrato, resulte intencionalmente falso o comprometa la integridad de la
                            Plataforma. La detección de violaciones recurrentes será causal para la revocación definitiva de la cuenta
                            del infractor.</p>
                      <p><strong>4.4. Otorgamiento de Licencias de Uso:</strong></p>
                      <ul>
                            <li>
                                  <p><strong>4.4.1. Hacia la Plataforma:</strong> El Usuario conserva sus derechos morales y
                                        patrimoniales. No obstante, otorga a Promoabastos una licencia perpetua, mundial, no exclusiva,
                                        exenta de regalías, transferible y sublicenciable para usar, reproducir, modificar, distribuir y
                                        exhibir públicamente el UGC en relación con la operación y promoción comercial de la Plataforma.
                                  </p>
                            </li>
                            <li>
                                  <p><strong>4.4.2. Hacia la Comunidad:</strong> El Usuario concede a los demás miembros de la
                                        Plataforma una licencia no exclusiva para acceder a su UGC, interactuar con él y compartirlo
                                        (interna o externamente) a través de las funcionalidades técnicas proporcionadas por el
                                        servicio.</p>
                            </li>
                      </ul>
                      <p><strong>4.5. Comercialización de la Información Factual:</strong> El Usuario reconoce que los datos fácticos
                            aportados (ej. precios, ubicaciones de locales, descripciones objetivas y disponibilidad de inventario) son
                            hechos desprovistos de derechos de autor (copyright). En consecuencia, Promoabastos adquiere el derecho
                            permanente e irrevocable de conservar, procesar, distribuir, licenciar y comercializar esta información
                            factual (p. ej., mediante la provisión de APIs empresariales, bases de datos o reportes de inteligencia
                            comercial), con total independencia de la vigencia de la cuenta del Usuario.</p>
                      <h2 id="compras-integrables-suscripciones-y-servicios-premium">5. COMPRAS INTEGRABLES, SUSCRIPCIONES Y SERVICIOS
                            PREMIUM</h2>
                      <p><strong>5.1. Funcionalidades de Pago:</strong> La Aplicación podrá integrar servicios premium, suscripciones o
                            compras integradas; incluyendo, de forma enunciativa mas no limitativa, el cobro a los usuarios por
                            resaltar, promocionar o dotar de mayor visibilidad algorítmica a ciertas publicaciones u ofertas.</p>
                      <p><strong>5.2. Procesamiento de Transacciones:</strong> Las tarifas, términos de facturación y políticas de
                            cancelación serán claramente estipuladas previo a cualquier transacción, procesándose bajo los estrictos
                            estándares de seguridad de las plataformas de pago (pasarelas) autorizadas.</p>
                      <p><strong>5.3. Modificación de Tarifas:</strong> Promoabastos se reserva el derecho absoluto de crear, alterar,
                            descontinuar servicios de pago o modificar sus esquemas tarifarios, lo cual será comunicado mediante aviso
                            previo razonable a los usuarios activos.</p>
                      <h2 id="restricciones-de-uso-y-protección-del-sistema">6. RESTRICCIONES DE USO Y PROTECCIÓN DEL SISTEMA</h2>
                      <p><strong>6.1. Usos Prohibidos:</strong> El Usuario se compromete a abstenerse de realizar, permitir o facilitar,
                            directa o indirectamente, las siguientes conductas:</p>
                      <ul>
                            <li>
                                  <p><strong>6.1.1.</strong> Licenciar, vender, arrendar, ceder o explotar comercialmente la Aplicación,
                                        total o parcialmente.</p>
                            </li>
                            <li>
                                  <p><strong>6.1.2.</strong> Alterar, crear obras derivadas, descompilar, aplicar ingeniería inversa o
                                        procurar extraer el código fuente de la Aplicación o de sus sistemas lógicos subyacentes.</p>
                            </li>
                            <li>
                                  <p><strong>6.1.3. Extracción y Distribución Ilegal (Scraping):</strong> Utilizar herramientas
                                        automatizadas (bots, <em>scrapers</em>, <em>crawlers</em>, arañas) o métodos manuales de
                                        extracción sistemática para descargar, recopilar, minar o distribuir los datos de ofertas, bases
                                        de datos de usuarios o recursos (assets) de la Plataforma con fines comerciales, de distribución
                                        masiva, o para la creación de productos derivados que compitan con la Plataforma.</p>
                            </li>
                            <li>
                                  <p><strong>6.1.4.</strong> Emplear la Plataforma o su información para facilitar conductas delictivas,
                                        fraudulentas o cualquier actividad penada por la jurisdicción mexicana o internacional.</p>
                            </li>
                      </ul>
                      <h2 id="propiedad-intelectual-e-industrial">7. PROPIEDAD INTELECTUAL E INDUSTRIAL</h2>
                      <p><strong>7.1. Derechos de Titularidad:</strong> Todo derecho, título, patente e interés sobre la Plataforma
                            (incluyendo de forma enunciativa su código fuente, algoritmos, arquitecturas de bases de datos, marcas,
                            logotipos e interfaz gráfica), con la excepción explícita del UGC aportado por los usuarios, pertenece de
                            manera exclusiva a Promoabastos, encontrándose resguardado bajo la legislación mexicana e internacional
                            aplicable en materia de Propiedad Intelectual e Industrial.</p>
                      <h2 id="limitación-de-responsabilidad-y-puerto-seguro">8. LIMITACIÓN DE RESPONSABILIDAD Y PUERTO SEGURO</h2>
                      <p><strong>8.1. Ausencia de Garantías:</strong> La Aplicación y sus servicios se suministran en su condición
                            actual (“TAL CUAL” o <em>AS IS</em>), sin garantías expresas o implícitas respecto a su disponibilidad
                            ininterrumpida, su funcionamiento libre de errores o la exactitud comercial del contenido provisto por
                            terceros.</p>
                      <p><strong>8.2. Cláusulas de Eximente (Safe Harbor):</strong> Promoabastos excluye explícita y categóricamente
                            cualquier responsabilidad civil, penal o administrativa emanada o vinculada a:</p>
                      <ul>
                            <li>
                                  <p><strong>8.2.1. Contenido de Terceros (UGC):</strong> La veracidad, seguridad, legalidad, calidad o
                                        precisión del contenido, enlaces u ofertas publicadas en la Plataforma por la comunidad de
                                        usuarios.</p>
                            </li>
                            <li>
                                  <p><strong>8.2.2. Uso Malintencionado:</strong> El uso indebido, fraudulento, no autorizado o
                                        perjudicial que terceros (sean usuarios registrados o no) hagan de la información pública o
                                        perfiles alojados abiertamente en la Plataforma.</p>
                            </li>
                      </ul>
                      <h2 id="legislación-aplicable-y-jurisdicción-competente">9. LEGISLACIÓN APLICABLE Y JURISDICCIÓN COMPETENTE</h2>
                      <p><strong>9.1. Marco Jurídico:</strong> La interpretación, validez y ejecución del presente Contrato se regirán
                            estrictamente por la legislación vigente de los Estados Unidos Mexicanos.</p>
                      <p><strong>9.2. Jurisdicción:</strong> Para cualquier controversia, demanda o litigio derivado del presente
                            Contrato, las partes acuerdan someterse de forma exclusiva y vinculante a la jurisdicción y competencia de
                            los tribunales correspondientes de la Ciudad de México, renunciando expresamente a cualquier otro fuero que
                            pudiera corresponderles en virtud de su domicilio presente o futuro.</p>
                      <h2 id="información-de-contacto-legal-y-técnico">10. INFORMACIÓN DE CONTACTO LEGAL Y TÉCNICO</h2>
                      <p><strong>10.1.</strong> Para remitir notificaciones formales, ejercer derechos o solicitar soporte técnico, el
                            Usuario podrá establecer comunicación a través de los siguientes medios autorizados:</p>
                      <ul>
                            <li>
                                  <p><strong>Correo electrónico legal:</strong> <a
                                              href="mailto:privacy@promoabastos.com">privacy@promoabastos.com</a></p>
                            </li>
                            <li>
                                  <p><strong>Sitio oficial:</strong> <a href="http://promoabastos.com">promoabastos.com</a></p>
                            </li>
                            <li>
                                  <p><strong>Soporte Institucional:</strong> Mediante cualquier canal de asistencia, portal web, o
                                        notificación oficial establecido y operado formalmente por Promoabastos o sus representantes
                                        legales.</p>
                            </li>
                      </ul>

                </body>

                </html>
                """;
    }
}
