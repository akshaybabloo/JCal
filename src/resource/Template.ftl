<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title></title>
        <#--<link rel="stylesheet" href="https://bootswatch.com/flatly/bootstrap.min.css">-->
        <link rel="stylesheet" href="resource/bootstrap.css">

        <style>.invoice-title h2, .invoice-title h3 {
            display: inline-block;
        }

        .table > tbody > tr > .no-line {
            border-top: none;
        }

        .table > thead > tr > .no-line {
            border-bottom: none;
        }

        .table > tbody > tr > .thick-line {
            border-top: 2px solid;
        }

        .navbar-default {
            background-color: #ECF0F1;
        }

        @media print {
            .navbar {
                display: block;
                border-width: 0 !important;
            }

            .navbar-toggle {
                display: none;
            }
        }</style>
        <!-- Latest compiled and minified JavaScript -->
        <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="invoice-title">
                        <h2>${companyName}</h2>

                        <#--<h3 class="pull-right">Order # 12345</h3>-->
                    </div>
                    <hr>
                    <div class="row">
                        <div class="col-xs-6">
                            <address>
                                <strong>To:</strong><br>
                                ${custFullName}<br>
                                ${custAddress}<br>
                            </address>
                        </div>
                        <div class="col-xs-6 text-right">
                            <address>
                                <strong>Data:</strong><br>
                                ${currentDate}
                            </address>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-xs-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title"><strong>Summary</strong></h3>
                        </div>
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-condensed table-bordered">
                                    <!--<thead>-->
                                    <tbody>
                                    <tr>
                                        <th><strong>Loan Amount</strong></th>
                                        <td class="text-center">${loanAmount}</td>
                                    </tr>
                                    <!--</thead>-->
                                    <!--<tbody>-->
                                    <!-- foreach ($order->lineItems as $line) or some such thing here -->
                                    <tr>
                                        <th>Years</th>
                                        <td class="text-center">${years}</td>
                                    </tr>
                                    <tr>
                                        <th>Months</th>
                                        <td class="text-center">${months}</td>
                                    </tr>
                                    <tr>
                                        <th>${typeOfPayments}</th>
                                        <td class="text-center">${payments}</td>
                                    </tr>
                                    <tr>
                                        <th>Total interest</th>
                                        <td class="text-center">${totalInterest}</td>
                                    </tr>
                                    <tr>
                                        <th>Total payments</th>
                                        <td class="text-center">${totalPayments}</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title"><strong>Pie chart</strong></h3>
                        </div>
                        <div class="panel-body">
                            <img class="img-responsive" src="Pie.png">
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title"><strong>Loan summary</strong></h3>
                        </div>
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-condensed table-bordered">
                                    <thead>
                                    <tr>
                                        <td><strong>${typeOfTime}</strong></td>
                                        <td class="text-center"><strong>Principal</strong></td>
                                        <td class="text-center"><strong>Interest</strong></td>
                                        <td class="text-right"><strong>Balance</strong></td>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        ${data}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-4">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title"><strong>Interest and Principal</strong></h3>
                        </div>
                        <div class="panel-body">
                            <img class="img-responsive" src="InterestAndPrincipal.png">
                        </div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title"><strong>Balance</strong></h3>
                        </div>
                        <div class="panel-body">
                            <img class="img-responsive" src="Balance.png">
                        </div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title"><strong>Annual Interest</strong></h3>
                        </div>
                        <div class="panel-body">
                            <img class="img-responsive" src="Interest.png">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="navbar navbar-default navbar-bottom" role="navigation">
            <div class="container">
                <div class="navbar-text pull-left">
                    &copy; ${year} ${companyName}. Contact person: ${contactName}, Address: ${contactAddress} and Contact number: ${contactNumber}
                    Fax: ${faxNumber}
                </div>
            </div>
        </div>
    </body>
</html>