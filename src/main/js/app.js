'use strict';

// tag::vars[]
const React = require('react'); // <1>
const ReactDOM = require('react-dom'); // <2>
const client = require('./client'); // <3>
const follow = require('./follow'); // function to hop multiple links by "rel"
const root = '/api';
// end::vars[]

// tag::app[]
class App extends React.Component { // <1>

	constructor(props) {
		super(props);
		this.state = {customers: [], purchases: []};
	}

	componentDidMount() { // <2>
		client({method: 'GET', path: '/api/customers'}).done(response => {
			this.setState({customers: response.entity._embedded.customers});
		});
		follow(client, root, [
			{rel: 'employees', params: {size: pageSize}}]
		).then(employeeCollection => {
			return client({
				method: 'GET',
				path: employeeCollection.entity._links.profile.href,
				headers: {'Accept': 'application/schema+json'}
			}).then(schema => {
				// tag::json-schema-filter[]
				/**
				 * Filter unneeded JSON Schema properties, like uri references and
				 * subtypes ($ref).
				 */
				Object.keys(schema.entity.properties).forEach(function (property) {
					if (schema.entity.properties[property].hasOwnProperty('format') &&
						schema.entity.properties[property].format === 'uri') {
						delete schema.entity.properties[property];
					}
					else if (schema.entity.properties[property].hasOwnProperty('$ref')) {
						delete schema.entity.properties[property];
					}
				});

				this.schema = schema.entity;
				this.links = employeeCollection.entity._links;
				return employeeCollection;
				// end::json-schema-filter[]
			});
		}).then(employeeCollection => {
			this.page = employeeCollection.entity.page;
			return employeeCollection.entity._embedded.employees.map(employee =>
				client({
					method: 'GET',
					path: employee._links.self.href
				})
			);
		}).then(employeePromises => {
			return when.all(employeePromises);
		}).done(employees => {
			this.setState({
				page: this.page,
				employees: employees,
				attributes: Object.keys(this.schema.properties),
				pageSize: pageSize,
				links: this.links
			});
		});
	}

	render() { // <3>
		return (
			<React.Fragment>
				<PurchaseList purchases={this.state.purchases}/>
				<CustomerList customers={this.state.customers}/>
			</React.Fragment>

		)
	}
}
// end::app[]

// tag::purchase-list[]
class PurchaseList extends React.Component{
	render() {
		const purchases = this.props.purchases.map(purchase =>
			<Purchase key={purchase._links.self.href} purchase={purchase}/>
		);
		return (
			<table>
				<tbody>
				<tr>
					<th>Purchase Date</th>
					<th>Product</th>
				</tr>
				{purchases}
				</tbody>
			</table>
		)
	}
}
// end::purchase-list[]

// tag::customer-list[]
class CustomerList extends React.Component{
	render() {
		const customers = this.props.customers.map(customer =>
			<Customer key={customer._links.self.href} customer={customer}/>
		);
		return (
			<table>
				<tbody>
					<tr>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Description</th>
					</tr>
					{customers}
				</tbody>
			</table>
		)
	}
}
// end::customer-list[]

// tag::purchase[]
class Purchase extends React.Component{
	render() {
		return (
			<tr>
				<td>{this.props.purchase.purchaseDate}</td>
				<td>{this.props.purchase.products}</td>

			</tr>
		)
	}
}
// end::purchase[]

// tag::customer[]
class Customer extends React.Component{
	render() {
		return (
			<tr>
				<td>{this.props.customer.firstName}</td>
				<td>{this.props.customer.lastName}</td>
				<td>{this.props.customer.description}</td>
			</tr>
		)
	}
}
// end::customer[]

// tag::render[]
ReactDOM.render(
	<App />,
	document.getElementById('react')
)
// end::render[]
